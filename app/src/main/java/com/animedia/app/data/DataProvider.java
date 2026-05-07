package com.animedia.app.data;

import com.animedia.app.model.AnimeItem;
import com.animedia.app.model.ClanItem;
import com.animedia.app.model.CollectionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataProvider {

    private static final String[] ANIME_NAMES = {
        "Сто видов Авадзимы",
        "Вас снова убили, мистер детектив",
        "Дорогой звёзд 7",
        "Безупречный мир",
        "Лепестки реинкарнации",
        "Континент силы и духа",
        "Расцвет молодости: Наша весна",
        "Дом, в котором щебечет Кузьма",
        "Доктор Стоун: Научное будущее",
        "Принцесса-рыцарь — невеста варвара",
        "Ледяная стена",
        "Вторая юность Хайбары",
        "Авантюрист, пожирающий демонов",
        "Фан-клуб Кирио",
        "Любовь кусается: Моя девушка — зомби",
        "Моя геройская академия: Больше",
        "Поглощенная звезда",
        "Извивающийся дракон",
        "Императорская наложница 2",
        "Иные стремления Чжи Тана",
        "Король ночи",
        "Однажды я стала принцессой",
        "Кулак Северной звезды (2026)",
        "Повелитель духов",
        "Противостояние святого",
        "Боевой континент 2",
        "Меч грядущего 2",
        "Регрессия великого мудреца"
    };

    private static final String[] GENRES = {
        "Экшн", "Романтика", "Фэнтези", "Комедия", "Драма",
        "Приключения", "Триллер", "Повседневность", "Детектив",
        "Меха", "Спорт", "Музыка", "Психология", "Ужасы"
    };

    private static final String[] STATUSES = {
        "Онгоинг", "Завершён", "Анонс"
    };

    private static final String[] COLLECTIONS = {
        "Романтичные Тайтлы",
        "Подборка Аниме",
        "Luvanime",
        "Тайтлы не для каждого",
        "Лучшее 2025",
        "Аниме для новичков"
    };

    private static final String[] TIMES = {
        "Сегодня", "Вчера", "2 дня назад", "3 дня назад"
    };

    private static final String[] COLORS = {
        "#6C3CE1", "#FF6B6B", "#FFB347", "#4CAF50", "#00BCD4",
        "#FF4081", "#7C4DFF", "#FF6E40", "#69F0AE", "#40C4FF"
    };

    private static final Random random = new Random();

    public static List<AnimeItem> getNewEpisodes() {
        List<AnimeItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AnimeItem item = new AnimeItem();
            item.setId(i + 1);
            item.setTitle(ANIME_NAMES[i % ANIME_NAMES.length]);
            item.setTimeInfo("Сегодня, " + (17 + (i % 6)) + ":" + String.format("%02d", i * 7) );
            int epNum = (i + 1) * 2 + random.nextInt(5);
            item.setEpisodeInfo(epNum + " серия");
            item.setEpisodeNumber(epNum);
            items.add(item);
        }
        return items;
    }

    public static List<AnimeItem> getTodayReleases() {
        List<AnimeItem> items = new ArrayList<>();
        String[] names = {"Ледяная стена", "Вторая юность Хайбары",
                "Авантюрист, пожирающий демонов", "Фан-клуб Кирио"};
        String[] times = {"22:30", "23:00", "23:00", "23:00"};
        for (int i = 0; i < names.length; i++) {
            AnimeItem item = new AnimeItem();
            item.setId(100 + i);
            item.setTitle(names[i]);
            item.setTimeInfo("Новая серия в " + times[i]);
            item.setEpisodeInfo("6 серия");
            item.setEpisodeNumber(6);
            items.add(item);
        }
        return items;
    }

    public static List<AnimeItem> getNewAnime() {
        List<AnimeItem> items = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            AnimeItem item = new AnimeItem();
            item.setId(200 + i);
            item.setTitle(ANIME_NAMES[(i + 5) % ANIME_NAMES.length]);
            item.setYear("2025");
            item.setRating(String.format("%.1f", 6.5 + random.nextFloat() * 3.5));
            item.setStatus(STATUSES[random.nextInt(STATUSES.length)]);
            item.setType(i % 2 == 0 ? "ТВ" : "ONA");
            item.setTotalEpisodes(12 + random.nextInt(24));
            items.add(item);
        }
        return items;
    }

    public static List<ClanItem> getTopClans() {
        List<ClanItem> clans = new ArrayList<>();
        clans.add(new ClanItem(1, "СЯО", "221 298", "298 участников", "#6C3CE1"));
        clans.add(new ClanItem(2, "FOR LIFETIME", "121 408", "408 участников", "#FF6B6B"));
        clans.add(new ClanItem(3, "Shadow Balans", "90 275", "275 участников", "#FFB347"));
        return clans;
    }

    public static List<CollectionItem> getCollections() {
        List<CollectionItem> items = new ArrayList<>();
        for (int i = 0; i < COLLECTIONS.length; i++) {
            items.add(new CollectionItem(i, COLLECTIONS[i], null));
        }
        return items;
    }

    public static AnimeItem getAnimeDetail(int id) {
        AnimeItem item = new AnimeItem();
        String name = ANIME_NAMES[id % ANIME_NAMES.length];
        item.setId(id);
        item.setTitle(name);
        item.setDescription("Захватывающая история о приключениях в мире, где реальность переплетается с фантазией. Главный герой отправляется в опасное путешествие, чтобы спасти своих близких и раскрыть тайну древнего проклятия. На его пути встретятся верные друзья и могущественные враги, а каждое решение будет иметь далеко идущие последствия.");
        item.setYear("2025");
        item.setRating(String.format("%.1f", 7.5 + random.nextFloat() * 2.0));
        item.setStatus("Онгоинг");
        item.setType("ТВ");
        item.setTotalEpisodes(24);
        item.setGenres(new String[]{"Экшн", "Фэнтези", "Приключения", "Драма"});
        return item;
    }

    public static String getRandomColor() {
        return COLORS[random.nextInt(COLORS.length)];
    }
}
