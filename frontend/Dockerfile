# Build
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# ajuste si ton projet s’appelle autrement que "koto-web"
RUN npx ng build --configuration=production

# Serve
FROM nginx:alpine
# NGINX conf avec proxy /api -> backend
COPY nginx.conf /etc/nginx/conf.d/default.conf
# copie le build Angular
COPY --from=build /app/dist/*/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
