import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
export default defineConfig({
    plugins: [react()],
    // La build finale viene servita da Spring Boot sotto /app (vedi WebConfig.java).
    base: '/app/',
    server: {
        port: 5173,
        // In sviluppo (npm run dev) le chiamate a /api vengono inoltrate al
        // backend Spring Boot su :8080 — stessa origine dal punto di vista del
        // browser, quindi nessun problema di CORS anche in questa modalità.
        proxy: {
            '/api': 'http://localhost:8080',
        },
    },
});
