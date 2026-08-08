# MLC Converter 🇨🇺

**MLC Converter** es una aplicación moderna de Android diseñada para facilitar el seguimiento y la conversión de divisas en el mercado informal cubano. Con un enfoque en la simplicidad, velocidad y precisión, la app permite a los usuarios convertir rápidamente entre **CUP, MLC, USD y EUR** utilizando las tasas de cambio más recientes.

## 🚀 Características Principales

- **Tasas en Tiempo Real**: Visualización de las tasas de cambio del mercado informal para las principales divisas (USD, EUR, MLC).
- **Conversor Inteligente**: Conversión bidireccional entre pesos cubanos (CUP) y divisas extranjeras.
- **Modo Oscuro/Claro**: Soporte completo para temas Claro y Oscuro, con opción de seguimiento automático del sistema.
- **Experiencia de Usuario Fluida**:
  - **Skeleton Loading**: Animaciones de carga que mejoran la respuesta visual.
  - **Manejo de Errores**: Sistema de reintentos y timeouts para garantizar que el usuario siempre sepa el estado de la conexión.
- **Diseño Moderno**: Interfaz basada en Material Design 3 con bordes suaves y paleta de colores profesional.

## 🛠️ Stack Tecnológico

La aplicación ha sido actualizada a los estándares más recientes de desarrollo Android (2024):

- **Lenguaje**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Arquitectura declarativa).
- **Arquitectura**: MVVM (Model-View-ViewModel) con Clean Architecture.
- **Inyección de Dependencias**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).
- **Networking**: [Retrofit](https://square.github.io/retrofit/) + OKHttp para consumo de APIs REST.
- **Gestión de Dependencias**: **Version Catalog (`libs.versions.toml`)** para un control centralizado de bibliotecas.
- **Persistencia Ligera**: [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) para preferencias del usuario (ej. Tema).
- **Carga de Imágenes**: [Coil](https://coil-kt.github.io/coil/) para renderizado eficiente.

## 🏗️ Arquitectura del Proyecto

El código está organizado siguiendo principios de separación de responsabilidades:

- `data/`: Implementaciones de API, servicios y modelos de red.
- `domain/`: Casos de uso y lógica de negocio pura.
- `di/`: Módulos de Hilt para la provisión de dependencias.
- `ui/`:
  - `pages/`: Pantallas principales en Compose.
  - `vm/`: ViewModels que gestionan el estado de la UI mediante `StateFlow`.
  - `theme/`: Definición de colores, tipografía y estilos globales.

## ⚙️ Configuración y Construcción

### Requisitos
- **Android Studio Jellyfish | 2023.3.1** o superior.
- **JDK 17** o superior.
- **Android SDK 36** (Target & Compile SDK).

### Pasos para Compilar
1. Clona el repositorio:
   ```bash
   git clone https://github.com/alfredoespal97/MLCConvertrer.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle para descargar las dependencias definidas en `libs.versions.toml`.
4. Ejecuta la aplicación en un emulador o dispositivo real (API 22+).

## 📈 Próximas Mejoras
- [ ] Gráficos históricos de las tasas de cambio.
- [ ] Notificaciones push cuando haya variaciones significativas en las tasas.
- [ ] Soporte multi-idioma.
- [ ] Widget para la pantalla de inicio.

---
Desarrollado con ❤️ para la comunidad.
