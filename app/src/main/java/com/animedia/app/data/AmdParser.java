package com.animedia.app.data;

import com.animedia.app.model.AnimeItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmdParser {

    private static final String BASE_URL = "https://amd.online";
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Mobile Safari/537.36";

    public static List<AnimeItem> parseNewEpisodes(String html) {
        List<AnimeItem> items = new ArrayList<>();
        if (html == null || html.isEmpty()) return items;

        Document doc = Jsoup.parse(html);

        // Find the new episodes section
        Element section = doc.selectFirst("#amd-poslednie-serii");
        if (section == null) {
            // Fallback: find all ftop-item links
            Elements ftopItems = doc.select("a.ftop-item");
            for (Element item : ftopItems) {
                AnimeItem anime = parseFtopItem(item);
                if (anime != null) items.add(anime);
            }
            return items;
        }

        Elements ftopItems = section.select("a.ftop-item");
        for (Element item : ftopItems) {
            AnimeItem anime = parseFtopItem(item);
            if (anime != null) items.add(anime);
        }

        return items;
    }

    private static AnimeItem parseFtopItem(Element item) {
        try {
            AnimeItem anime = new AnimeItem();

            // Detail URL
            String href = item.attr("href");
            if (href.isEmpty()) return null;
            anime.setDetailUrl(href);

            // Extract ID from URL like /2246-vtoraja-junost-hajbary.html
            Matcher idMatcher = Pattern.compile("/(\\d+)-").matcher(href);
            if (idMatcher.find()) {
                anime.setId(idMatcher.group(1));
            }

            // Poster
            Element imgEl = item.selectFirst("img");
            if (imgEl != null) {
                String src = imgEl.attr("src");
                if (!src.isEmpty()) anime.setPosterUrl(src);
            }

            // Title
            Element titleEl = item.selectFirst(".ftop-item__title");
            if (titleEl != null) {
                anime.setTitle(titleEl.text().trim());
            }

            // Time info
            Element metaEl = item.selectFirst(".ftop-item__meta");
            if (metaEl != null) {
                anime.setTimeInfo(metaEl.text().trim());
            }

            // Episode info
            Element epEl = item.selectFirst(".animseri");
            if (epEl != null) {
                String epText = epEl.text().trim(); // "6 серия"
                anime.setEpisodeInfo(epText);
                Matcher numMatcher = Pattern.compile("(\\d+)").matcher(epText);
                if (numMatcher.find()) {
                    anime.setEpisodeNumber(Integer.parseInt(numMatcher.group(1)));
                }
            }

            return anime;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<AnimeItem> parseAnimeList(String html) {
        List<AnimeItem> items = new ArrayList<>();
        if (html == null || html.isEmpty()) return items;

        Document doc = Jsoup.parse(html);

        // Parse poster cards (carousel and grid items)
        Elements posterItems = doc.select("div.poster");
        for (Element poster : posterItems) {
            AnimeItem anime = parsePosterCard(poster);
            if (anime != null && anime.getTitle() != null) items.add(anime);
        }

        // Also parse shortstory items (grid list)
        Elements shortItems = doc.select("div.shortstory");
        for (Element item : shortItems) {
            AnimeItem anime = parseShortStory(item);
            if (anime != null && anime.getTitle() != null) items.add(anime);
        }

        return items;
    }

    private static AnimeItem parsePosterCard(Element poster) {
        try {
            AnimeItem anime = new AnimeItem();

            // Link
            Element linkEl = poster.selectFirst("a.poster__link, a");
            if (linkEl != null) {
                String href = linkEl.attr("href");
                anime.setDetailUrl(href);
                Matcher idMatcher = Pattern.compile("/(\\d+)-").matcher(href);
                if (idMatcher.find()) anime.setId(idMatcher.group(1));
            }

            // Title
            Element titleEl = poster.selectFirst(".poster__title, h3");
            if (titleEl != null) anime.setTitle(titleEl.text().trim());

            // Image
            Element imgEl = poster.selectFirst("img");
            if (imgEl != null) {
                String src = imgEl.attr("src");
                if (!src.isEmpty()) anime.setPosterUrl(src);
            }

            return anime;
        } catch (Exception e) {
            return null;
        }
    }

    private static AnimeItem parseShortStory(Element item) {
        try {
            AnimeItem anime = new AnimeItem();

            // Link
            Element linkEl = item.selectFirst("a");
            if (linkEl != null) {
                String href = linkEl.attr("href");
                if (href.contains("/")) {
                    anime.setDetailUrl(href);
                    Matcher idMatcher = Pattern.compile("/(\\d+)-").matcher(href);
                    if (idMatcher.find()) anime.setId(idMatcher.group(1));
                }
            }

            // Title
            Element titleEl = item.selectFirst("h2, h3, .shortstory__title");
            if (titleEl != null) {
                anime.setTitle(titleEl.text().trim());
            }

            // Image
            Element imgEl = item.selectFirst("img");
            if (imgEl != null) {
                String src = imgEl.attr("src");
                if (!src.isEmpty()) anime.setPosterUrl(src);
            }

            return anime;
        } catch (Exception e) {
            return null;
        }
    }

    public static AnimeItem parseAnimeDetail(String html) {
        if (html == null || html.isEmpty()) return null;

        try {
            Document doc = Jsoup.parse(html);
            AnimeItem anime = new AnimeItem();

            // Title (h1)
            Element h1 = doc.selectFirst("h1");
            if (h1 != null) {
                anime.setTitle(h1.text().trim());
            }

            // Sub title / original name
            Element subEl = doc.selectFirst(".amd-sub");
            if (subEl != null) {
                anime.setSubTitle(subEl.text().trim());
            }

            // Poster image - from amd-poster area
            Element posterEl = doc.selectFirst(".amd-poster");
            if (posterEl != null) {
                Element imgEl = posterEl.selectFirst("img");
                if (imgEl != null) {
                    String src = imgEl.attr("src");
                    if (!src.isEmpty()) anime.setPosterUrl(src);
                }
            }
            // Fallback: find poster in amd-left
            if (anime.getPosterUrl() == null || anime.getPosterUrl().isEmpty()) {
                Element leftEl = doc.selectFirst(".amd-left");
                if (leftEl != null) {
                    Elements imgs = leftEl.select("img");
                    for (Element img : imgs) {
                        String src = img.attr("src");
                        if (src.contains("/uploads/posts/")) {
                            anime.setPosterUrl(src);
                            break;
                        }
                    }
                }
            }

            // Rating
            Element scoreEl = doc.selectFirst(".amd-score");
            if (scoreEl != null) {
                anime.setRating(scoreEl.text().trim());
            }

            // Genres / tags
            Element tagsEl = doc.selectFirst(".amd-tags");
            if (tagsEl != null) {
                Elements tagLinks = tagsEl.select("a");
                List<String> genres = new ArrayList<>();
                for (Element tag : tagLinks) {
                    String genre = tag.text().trim();
                    if (!genre.isEmpty()) genres.add(genre);
                }
                if (!genres.isEmpty()) {
                    anime.setGenres(genres.toArray(new String[0]));
                }
            }

            // Meta info (year, status, type, studio)
            Element metaEl = doc.selectFirst(".amd-meta");
            if (metaEl != null) {
                Elements metaLinks = metaEl.select("a");
                if (metaLinks.size() > 0) anime.setYear(metaLinks.get(0).text().trim());
                if (metaLinks.size() > 1) anime.setStatus(metaLinks.get(1).text().trim());
                // Type is usually 4th link
                for (Element link : metaLinks) {
                    String text = link.text().trim();
                    if (text.contains("ТВ") || text.contains("ONA") || text.contains("OVA") || text.contains("Фильм")) {
                        anime.setType(text);
                    }
                    if (text.toLowerCase().contains("studio") || text.contains("comet") || text.contains("pierrot") || text.contains("mappa") || text.contains("ufotable") || text.contains(" bones") || text.contains("wit")) {
                        anime.setStudio(text);
                    }
                }
                // Season
                for (Element link : metaLinks) {
                    String text = link.text().trim();
                    if (text.contains("202") || text.contains("Зима") || text.contains("Весна") || text.contains("Лето") || text.contains("Осень")) {
                        if (text.contains("Весна") || text.contains("Зима") || text.contains("Лето") || text.contains("Осень")) {
                            anime.setSeason(text);
                        }
                    }
                }
            }

            // Episode count
            Element epCountEl = doc.selectFirst(".amd-vser");
            if (epCountEl != null) {
                String text = epCountEl.text().trim();
                anime.setTotalEpisodesText(text);
                Matcher numMatcher = Pattern.compile("из\\s*(\\d+)").matcher(text);
                if (numMatcher.find()) {
                    anime.setTotalEpisodes(Integer.parseInt(numMatcher.group(1)));
                } else {
                    Matcher numMatcher2 = Pattern.compile("(\\d+)").matcher(text);
                    if (numMatcher2.find()) {
                        anime.setTotalEpisodes(Integer.parseInt(numMatcher2.group(1)));
                    }
                }
            }

            // Description
            Element descEl = doc.selectFirst("#amd-desc, .amd-description");
            if (descEl != null) {
                anime.setDescription(descEl.html().trim());
            }

            return anime;
        } catch (Exception e) {
            return null;
        }
    }

    public static String fetchUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(15000)
                    .followRedirects(true)
                    .maxBodySize(0)
                    .get();
            return doc.html();
        } catch (Exception e) {
            return "";
        }
    }
}
