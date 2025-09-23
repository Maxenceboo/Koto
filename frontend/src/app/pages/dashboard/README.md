# Dashboard – Architecture et fonctionnement

Ce document décrit la structure, les responsabilités et les flux de données du Dashboard (canaux, messages/citations, membres, et actions utilisateur).

## Vue d’ensemble

- Container: `DashboardPage` gère tout l’état local (channels, membres, messages, utilisateur courant) et passe des données/événements aux sous‑composants.
- Subviews:
  - Left: sélection/ajout de canaux (`DashboardLeft`).
  - Main: fil des citations + composer + quitter le canal (`DashboardMain`).
  - Right: informations/membres du canal (`DashboardRight`).
  - Dialogs: ajout de canal (`AddChannelDialog`), confirmation de départ (`LeaveDialog`).

## Modèles & état (source de vérité)

- Fichier: `frontend/src/app/pages/dashboard/dashboard.ts`
  - Interfaces:
    - `Channel { id: number; name: string; }`
    - `Member { id: number; name: string; }`
    - `Message { id: number; text: string; authorId: number; createdAt: Date; likes: number; attributedTo: Member; }`
  - État local:
    - `channels: Channel[]` – liste des canaux visibles.
    - `selectedChannel?: Channel` – canal sélectionné.
    - `currentUserId = 1` – utilisateur courant (mock).
    - `channelMembers: Record<number, Member[]>` – membres par canal (tous les membres n’ont pas tous les canaux).
    - `messagesStore: Map<number, Message[]>` – messages par canal.
    - `nextMessageId` – séquence locale pour les IDs de message.
  - Getters:
    - `selectedMembers: Member[]` – membres du canal courant.
    - `selectedMessages: Message[]` – messages du canal courant.
  - Actions:
    - `onChannelSelect(channel)` – change la sélection.
    - `onJoinByUrl(url)` / `onCreateChannel({ name })` – ajout d’un canal (mock), initialise `channelMembers` et `messagesStore`.
    - `onLeaveChannel(channel)` – supprime le canal et nettoie les stores associés.
    - `onCreateMessage({ text, attributedTo })` – ajoute un message pour le canal courant avec `authorId = currentUserId`.
    - `onDeleteMessage(messageId)` – supprime un message par id (seulement côté UI; la règle d’affichage empêche la suppression par un autre auteur).

## Routage et injection

- Fichier: `frontend/src/app/app.routes.ts` – route `/dashboard` charge `DashboardPage`.
- Fichier: `frontend/src/app/app.config.ts` – fournit `provideHttpClient`, PrimeNG (thème Aura) et animations.

## Sous‑composants

### Left – Channels

- Fichiers: 
  - `frontend/src/app/pages/dashboard/components/left-side/left-side.ts`
  - `frontend/src/app/pages/dashboard/components/left-side/left-side.html`
- Inputs: `channels`, `selectedChannel`
- Outputs: `channelSelect`, `joinChannelByUrl`, `createChannel`
- UI: `p-listbox` pour sélectionner un canal, bouton “Add” qui ouvre `AddChannelDialog`.
- Dialog d’ajout: 
  - Fichiers: `components/dialogs/add-channel-dialog/*`
  - Permet via `SelectButton` de choisir méthode (URL vs Create) et d’émettre `joinByUrl`/`create`.

### Main – Fil + Composer + Quitter

- Fichiers:
  - `frontend/src/app/pages/dashboard/components/main-feed/main-feed.ts`
  - `frontend/src/app/pages/dashboard/components/main-feed/main-feed.html`
- Inputs: `channel`, `members`, `currentUserId`, `messages`
- Outputs: `sendMessage`, `deleteMessage`, `leaveChannel`
- Composants embarqués:
  - Composer: `components/message-composer/*`
  - Liste: `components/message-list/*`
  - Dialog quitter: `components/dialogs/leave-dialog/*`
- Règles métier (UI):
  - L’auteur n’est pas affiché, mais seulement l’auteur (`authorId === currentUserId`) voit le bouton Supprimer.
  - Attribution d’une citation obligatoire à un `Member`.

#### Message Composer

- Fichiers: `components/message-composer/message-composer.ts|html`
- Inputs: `members: Member[]`
- Output: `send: { text: string; attributedTo: Member }`
- UI: `p-autoComplete` (PrimeNG) avec `[dropdown]="true"`, suggestions filtrées via `completeMethod`, champ texte, bouton “Envoyer”.

#### Message List

- Fichiers: `components/message-list/message-list.ts|html`
- Inputs: `messages: Message[]`, `currentUserId: number`
- Output: `delete: number` (id du message)
- UI: Angular @for, DatePipe, bouton corbeille conditionnel (auteur‑only), affichage: `→ destinataire — date` + texte.

### Right – Infos/Membres

- Fichiers:
  - `frontend/src/app/pages/dashboard/components/right-side/right-side.ts|html`
- Input: `members: Member[]` – membres du canal sélectionné
- UI: rendu avec `@for`.

### Dialogs

- LeaveDialog – confirmation pour quitter un canal
  - Fichiers: `components/dialogs/leave-dialog/*`
  - Inputs: `visible`, `channel`; Outputs: `confirm`, `cancel`
  - UI: `p-dialog`
- AddChannelDialog – rejoindre ou créer un canal
  - Fichiers: `components/dialogs/add-channel-dialog/*`
  - Inputs: `visible`; Outputs: `visibleChange`, `cancel`, `joinByUrl`, `create`
  - UI: `p-dialog`, `p-selectButton`, `p-inputText`

## Navbar (Notifications)

- Fichiers: 
  - `frontend/src/app/shared/ui/navbar/navbar.ts|html`
- UI: bouton cloche qui ouvre un `p-popover` listant des notifications mock (avec DatePipe).

## Flux de données

1. Dashboard calcule `selectedMembers` et `selectedMessages` selon le `selectedChannel`.
2. `main-feed` affiche la liste et transmet les actions vers Dashboard via `sendMessage` / `deleteMessage` / `leaveChannel`.
3. Dashboard met à jour `messagesStore` (création/suppression).

## Extensions futures

- Brancher les API backend (auth, channels, membres, messages).
- Persistance des notifications et intégration temps réel.
- Gestion des likes et des commentaires sur les citations.
- Gestion des rôles (ex: modérateurs de canal).

## Notes techniques

- Angular 19 control flow utilisé (`@for`, `@if`).
- PrimeNG V20 utilisé (standalone components/modules selon disponibilité du package).
- HTTP global via `provideHttpClient()` (fichier `app.config.ts`).

