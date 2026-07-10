# android-cli

A powerful Kotlin-based Command Line Interface (CLI) that streamlines Android development by generating clean architecture boilerplate with a single command.

## ✨ Features

- 🚀 **Fast & Lightweight** – Built with Kotlin for fast execution.
- 📦 **Easy Installation** – Install on Windows with a single PowerShell command.
- 🔄 **Automatic Updates** – Always installs the latest stable release from GitHub.
- 🏗️ **Clean Architecture Generation** – Creates a complete feature structure automatically.
- 🔍 **Package Detection** – Detects your project's base package and source directory automatically.
- 🧩 **Ready-to-Use Code** – Generates boilerplate with correct package names and imports.

---

## 📥 Installation

### Windows (PowerShell)

Open **PowerShell** and run:

```powershell
irm https://raw.githubusercontent.com/sina-nakhaei/android-cli/master/install.ps1 | iex
```

---

## 🚀 Usage

Run commands from the root of your Android project.

### Check Version

```powershell
android -v
```

### View Help

```powershell
android --help
```

### Create a Feature

```powershell
android create-feature Auth
```

---

## 📁 Generated Structure

Running:

```powershell
android create-feature Auth
```

generates:

```text
auth/
├── data/
│   ├── datasource/
│   │   ├── local/
│   │   │   ├── AuthLocalDataSource.kt
│   │   │   └── AuthLocalDataSourceImpl.kt
│   │   └── remote/
│   │       ├── AuthRemoteDataSource.kt
│   │       └── AuthRemoteDataSourceImpl.kt
│   └── repository/
│       └── AuthRepositoryImpl.kt
├── domain/
│   └── repository/
│       └── AuthRepository.kt
└── presentation/
    ├── AuthRoute.kt
    ├── AuthScreen.kt
    ├── AuthViewModel.kt
    └── model/
        └── AuthUiState.kt
```

---

## ⚙️ How It Works

`android-cli` automatically:

- Locates your project's `src/main/java` or `src/main/kotlin` directory.
- Detects your application's base package.
- Creates the feature folder using lowercase naming (`auth`).
- Generates all required files with correct package declarations.
- Wires generated classes together with the proper imports.

No configuration required—just run the command inside your Android project.
