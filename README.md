# 🎬 MovieBrowser

MovieBrowser — Android-застосунок для перегляду популярних фільмів з TMDB API з можливістю додавати фільми в улюблені.

---

## ▶ Як скопіювати та запустити

### 1) Клонувати репозиторій
```sh
git clone https://github.com/ione-g/moviebrowser
cd moviebrowser
```

### 2) Створіть або відредагуйте файл local.properties у корені проєкту:

```sh
echo 'TMDB_TOKEN=PASTE_YOUR_TMDB_BEARER_TOKEN_HERE' >> local.properties
echo 'TMDB_BASE_URL=https://api.themoviedb.org/3/' >> local.properties
```
Згенерувати токен можна за посиланням [TMDB API Documentation](https://developer.themoviedb.org/)


### 3) Запустити застосунок
```sh
./gradlew :composeApp:installDebug
```

#### 4) Можна завантажити .apk
