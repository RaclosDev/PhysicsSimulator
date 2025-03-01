# Simulador de Leyes Físicas

Un simulador interactivo que permite visualizar y experimentar con diferentes leyes de gravedad y cuerpos en movimiento. Incluye una interfaz gráfica intuitiva (GUI) y un modo de línea de comandos para operaciones por lotes.

## 🚀 Características Principales

### **Leyes de Gravedad Implementadas**
- **Ley de Gravitación Universal de Newton**: Simula la fuerza gravitatoria entre cuerpos masivos.
- **Gravedad hacia el Centro**: Todos los cuerpos aceleran hacia el punto central (0,0).
- **Sin Gravedad**: Permite movimiento rectilíneo uniforme sin influencias gravitatorias.

### **Tipos de Cuerpos**
- **Cuerpo Básico**: Cuerpo con masa constante.
- **Cuerpo con Pérdida de Masa**: Simula objetos que pierden masa periódicamente según factores y frecuencias configurables.

### **Interfaz Gráfica (GUI)**
- **Vista de Tabla**: Visualización detallada de cuerpos con datos de posición, velocidad y masa.
- **Vista Gráfica**: Representación visual 2D de los cuerpos con opciones de zoom y ayuda interactiva.
- **Panel de Control**: 
  - Importación de datos desde archivos JSON.
  - Selección flexible de leyes de gravedad.
  - Configuración de parámetros de simulación (pasos, tiempo delta, delay).
  - Controles intuitivos para ejecución, pausa y reinicio.

## 📦 Dependencias

- **Apache Commons CLI**: Gestión avanzada de argumentos por línea de comandos.
- **org.json**: Biblioteca para procesamiento eficiente de archivos JSON.
- **Java Swing**: Framework para la creación de interfaces gráficas.

## ⚙️ Configuración

### Requisitos
- Java 11 o superior.
- Maven para la gestión de dependencias.

### Instalación
```bash
git clone [URL_DEL_REPOSITORIO]
cd physics-simulator
mvn clean install
```

## 🖥️ Modo de Uso

### Argumentos de Línea de Comandos

| Parámetro | Descripción |
|-----------|-------------|
| -i \<file\> | Archivo de entrada con cuerpos (formato JSON). |
| -o \<file\> | Archivo de salida para resultados (formato JSON). |
| -s \<steps\> | Número de pasos de simulación. |
| -dt \<value\> | Tiempo delta por paso (por defecto: 2500). |
| -gl \<type\> | Ley de gravedad: ftcg (centro), nlug (Newton), ng (sin gravedad). |
| -m \<mode\> | Modo de ejecución (batch o gui). |
| -h | Muestra ayuda detallada. |

### Ejemplos

**Modo Batch (Consola):**
```bash
java -jar simulator.jar -i input.json -o output.json -s 1000 -gl nlug
```

**Modo GUI:**
```bash
java -jar simulator.jar -m gui
```

## 🎮 Interfaz Gráfica (GUI)

### Funcionalidades

**Cargar Archivo:**
- Botón Open (📂) para seleccionar archivo JSON de cuerpos.
- Formato JSON esperado:
```json
{
  "bodies": [
    {"id": "earth", "mass": 5.97e24, "pos": [0,0], "vel": [0,0]}
  ]
}
```

**Seleccionar Ley de Gravedad:**
- Botón Physics (🌌) para elegir entre las leyes disponibles.

**Controles de Simulación:**
- Run (▶️): Inicia la simulación.
- Stop (⏹️): Detiene la simulación.
- Delay: Tiempo entre pasos (ms).
- Steps: Número de pasos a ejecutar.
- Delta Time: Tiempo real por paso.

**Vista Gráfica:**
- Zoom: + (zoom in), - (zoom out), = (ajuste automático).
- Ayuda: Presiona h para mostrar/ocultar información.

## 🗂️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   ├── simulator/
│   │   │   ├── launcher/       # Punto de entrada (Main)
│   │   │   ├── control/        # Controlador (Controller)
│   │   │   ├── model/          # Modelos (Body, GravityLaws)
│   │   │   ├── factories/      # Factories para cuerpos y leyes
│   │   │   ├── view/           # Componentes de la GUI
│   │   │   └── misc/           # Utilidades (Vector)
│   └── resources/              # Archivos de ejemplo
```

## 🛠️ Build & Ejecución

**Compilar con Maven:**
```bash
mvn package
```

**Ejecutar en Modo GUI:**
```bash
java -cp target/simulator.jar simulator.launcher.Main -m gui
```

**Ejecutar en Modo Batch:**
```bash
java -cp target/simulator.jar simulator.launcher.Main -i input.json -o output.json -s 500 -gl ftcg
```
