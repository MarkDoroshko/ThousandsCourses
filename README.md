# Thousands Courses

Тестовый проект для компании Effective Mobile. Поддерживает заглушечную авторизацию, получение курсов 
по API и добавление/удаление их из избранных, которые хранятся в локальной БД.

## Стек технологий

- **Jetpack Compose**
- **Material 3**
- **Hilt**
- **Room**
- **Retrofit**
- **Coroutines + Flow**
- **Navigation Compose**
- **Kotlinx Serialization**

Архитектура: **MVI + Clean Architecture**.

## Требования

- **Android Studio** Meerkat (2025.1) или новее
- **JDK 21+**
- **Android SDK 36** (установится автоматически через SDK Manager)
- Устройство или эмулятор с **Android 9.0 (API 28)** или выше

## Сборка и запуск

Актуальный релиз можно найти в `app/release/app-release.apk`.

1. Склонировать репозиторий и открыть корневую папку проекта в Android Studio.
2. Дождаться завершения Gradle Sync.
3. Выбрать конфигурацию **app** и нажать **Run**.

Сборка release-APK через терминал:

```bash
./gradlew assembleRelease
```

Собранный APK появится в `app/build/outputs/apk/release/`.

## Объяснение выбора технологий вопреки рекомендуемому стеку

1. `MVI` вместо `MVVM`

MVI был выбран как наилучший паттерн по моему мнению, потому что контракт общения между View и 
ViewModel строгий, то есть при работе с sealed interface компилятор гарантирует исчерпывающий when, что
защищает от необработанных действий, хотя в MVVM его нет на уровне языка.

2. `Jetpack Compose` вместо `XML`

XML-подход значительно более громоздкий и многословный по сравнению с декларативным Compose.