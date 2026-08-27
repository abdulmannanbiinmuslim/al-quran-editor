package com.example.data.repository

import com.example.data.model.AyahItem
import com.example.data.model.WordItem

object QuranSurahsStore {

    // Surah Al-Falaq (113) - 5 Ayahs
    val falaqAyahs = listOf(
        AyahItem(
            surahNumber = 113,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 6226,
            textUthmani = "قُلْ أَعُوذُ بِرَبِّ ٱلْفَلَقِ",
            textIndopak = "قُلْ اَعُوْذُ بِرَبِّ الْفَلَقِ",
            englishTranslation = "Say, \"I seek refuge in the Lord of daybreak,",
            banglaTranslation = "বলুন, আমি আশ্রয় প্রার্থনা করছি উষার রবের,",
            banglaTafsir = "সূরা আল-ফালাক সকল প্রকার অনিষ্ট থেকে আল্লাহর কাছে আশ্রয় চাওয়ার দোয়া।",
            words = listOf(WordItem(1, "قُلْ", "Say", "বলুন"), WordItem(2, "أَعُوذُ", "I seek refuge", "আমি আশ্রয় চাই"), WordItem(3, "بِرَبِّ ٱلْفَلَقِ", "in Lord of daybreak", "উষার রবের নিকট")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 555,
            defaultStartMs = 4000L, defaultEndMs = 9000L
        ),
        AyahItem(
            surahNumber = 113,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 6227,
            textUthmani = "مِن شَرِّ مَا خَلَقَ",
            textIndopak = "مِنْ شَرِّ مَا خَلَقَ",
            englishTranslation = "From the evil of that which He created,",
            banglaTranslation = "তিনি যা সৃষ্টি করেছেন তার অনিষ্ট হতে,",
            banglaTafsir = "আল্লাহর সৃষ্টির সকল দৃশ্য-অদৃশ্য অমঙ্গল ও ক্ষতি থেকে সুরক্ষা।",
            words = listOf(WordItem(1, "مِن شَرِّ", "From the evil", "অনিষ্ট হতে"), WordItem(2, "مَا خَلَقَ", "of what He created", "তিনি যা সৃষ্টি করেছেন")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 555,
            defaultStartMs = 9000L, defaultEndMs = 13000L
        ),
        AyahItem(
            surahNumber = 113,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 6228,
            textUthmani = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            textIndopak = "وَمِنْ شَرِّ غَاسِقٍ اِذَا وَقَبَ",
            englishTranslation = "And from the evil of darkness when it settles,",
            banglaTranslation = "এবং অন্ধকার রাতের অনিষ্ট হতে, যখন তা সমাগত হয়,",
            banglaTafsir = "রাত্রির অন্ধকারে ছড়িয়ে পড়া অনিষ্ট ও বিপদ থেকে আশ্রয় প্রার্থনা।",
            words = listOf(WordItem(1, "وَمِن شَرِّ", "And from evil", "এবং অনিষ্ট হতে"), WordItem(2, "غَاسِقٍ", "of darkness", "অন্ধকারের"), WordItem(3, "إِذَا وَقَبَ", "when settles", "যখন তা সমাগত হয়")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 555,
            defaultStartMs = 13000L, defaultEndMs = 18000L
        ),
        AyahItem(
            surahNumber = 113,
            ayahNumberInSurah = 4,
            ayahNumberInQuran = 6229,
            textUthmani = "وَمِن شَرِّ ٱلنَّفَّٰثَٰتِ فِى ٱلْعُقَدِ",
            textIndopak = "وَمِنْ شَرِّ النَّفّٰثٰتِ فِی الْعُقَدِ",
            englishTranslation = "And from the evil of the blowers in knots,",
            banglaTranslation = "এবং গ্রন্থিতে ফুঁৎকারকারী নারীদের অনিষ্ট হতে,",
            banglaTafsir = "জাদু-টোনার অপশক্তি ও চক্রান্ত থেকে আল্লাহর আশ্রয়।",
            words = listOf(WordItem(1, "وَمِن شَرِّ", "And from evil", "এবং অনিষ্ট হতে"), WordItem(2, "ٱلنَّفَّٰثَٰتِ", "the blowers", "ফুঁৎকারকারিণীদের"), WordItem(3, "فِى ٱلْعُقَدِ", "in knots", "গ্রন্থিতে")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 555,
            defaultStartMs = 18000L, defaultEndMs = 23000L
        ),
        AyahItem(
            surahNumber = 113,
            ayahNumberInSurah = 5,
            ayahNumberInQuran = 6230,
            textUthmani = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            textIndopak = "وَمِنْ شَرِّ حَاسِدٍ اِذَا حَسَدَ",
            englishTranslation = "And from the evil of an envier when he envies.",
            banglaTranslation = "এবং হিংসুকের অনিষ্ট হতে, যখন সে হিংসা করে।",
            banglaTafsir = "হিংসা ও কুদৃষ্টির ক্ষতি থেকে আত্মরক্ষার মোক্ষম দোয়া।",
            words = listOf(WordItem(1, "وَمِن شَرِّ", "And from evil", "এবং অনিষ্ট হতে"), WordItem(2, "حَاسِدٍ", "of envier", "হিংসুকের"), WordItem(3, "إِذَا حَسَدَ", "when he envies", "যখন সে হিংসা করে")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 555,
            defaultStartMs = 23000L, defaultEndMs = 29000L
        )
    )

    // Surah An-Nas (114) - 6 Ayahs
    val nasAyahs = listOf(
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 6231,
            textUthmani = "قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ",
            textIndopak = "قُلْ اَعُوْذُ بِرَبِّ النَّاسِ",
            englishTranslation = "Say, \"I seek refuge in the Lord of mankind,",
            banglaTranslation = "বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের রবের,",
            banglaTafsir = "সূরা আন-নাস শয়তানের প্ররোচনা ও কুমন্ত্রণা থেকে সুরক্ষা লাভের দোয়া।",
            words = listOf(WordItem(1, "قُلْ", "Say", "বলুন"), WordItem(2, "أَعُوذُ", "I seek refuge", "আমি আশ্রয় চাই"), WordItem(3, "بِرَبِّ ٱلنَّاسِ", "in Lord of mankind", "মানুষের রবের")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 4000L, defaultEndMs = 9000L
        ),
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 6232,
            textUthmani = "مَلِكِ ٱلنَّاسِ",
            textIndopak = "مَلِكِ النَّاسِ",
            englishTranslation = "The Sovereign of mankind,",
            banglaTranslation = "মানুষের অধিপতির,",
            banglaTafsir = "আল্লাহ মানুষের একমাত্র সার্বভৌম রাজা ও প্রকৃত মালিক।",
            words = listOf(WordItem(1, "مَلِكِ", "King", "অধিপতি"), WordItem(2, "ٱلنَّاسِ", "of mankind", "মানুষের")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 9000L, defaultEndMs = 13000L
        ),
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 6233,
            textUthmani = "إِلَٰهِ ٱلنَّاسِ",
            textIndopak = "اِلٰهِ النَّاسِ",
            englishTranslation = "The God of mankind,",
            banglaTranslation = "মানুষের সত্য ইলাহের,",
            banglaTafsir = "মানবজাতির একমাত্র সত্য উপাস্য ও আশ্রয়স্থল।",
            words = listOf(WordItem(1, "إِلَٰهِ", "God", "ইলাহ"), WordItem(2, "ٱلنَّاسِ", "of mankind", "মানুষের")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 13000L, defaultEndMs = 17000L
        ),
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 4,
            ayahNumberInQuran = 6234,
            textUthmani = "مِن شَرِّ ٱلْوَسْوَاسِ ٱلْخَنَّاسِ",
            textIndopak = "مِنْ شَرِّ الْوَسْوَاسِ ۬ الْخَنَّاسِ",
            englishTranslation = "From the evil of the retreating whisperer -",
            banglaTranslation = "আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট হতে,",
            banglaTafsir = "শয়তান যখনই মানুষ আল্লাহকে স্মরণ করে তখন পিছিয়ে যায় এবং গাফেল হলেই কুমন্ত্রণা দেয়।",
            words = listOf(WordItem(1, "مِن شَرِّ", "From evil", "অনিষ্ট হতে"), WordItem(2, "ٱلْوَسْوَاسِ", "the whisperer", "কুমন্ত্রণাদাতার"), WordItem(3, "ٱلْخَنَّاسِ", "the retreating", "আত্মগোপনকারী")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 17000L, defaultEndMs = 23000L
        ),
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 5,
            ayahNumberInQuran = 6235,
            textUthmani = "ٱلَّذِى يُوَسْوِسُ فِى صُدُورِ ٱلنَّاسِ",
            textIndopak = "الَّذِیْ یُوَسْوِسُ فِیْ صُدُوْرِ النَّاسِ",
            englishTranslation = "Who whispers into the breasts of mankind -",
            banglaTranslation = "যে মানুষের অন্তরে কুমন্ত্রণা দেয়,",
            banglaTafsir = "অন্তরকে সন্দেহে ও বিভ্রান্তিতে ফেলে নেক আমল থেকে বিরত রাখার অপচেষ্টা।",
            words = listOf(WordItem(1, "ٱلَّذِى", "Who", "যে"), WordItem(2, "يُوَسْوِسُ", "whispers", "কুমন্ত্রণা দেয়"), WordItem(3, "فِى صُدُورِ", "in breasts", "অন্তরে"), WordItem(4, "ٱلنَّاسِ", "of mankind", "মানুষের")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 23000L, defaultEndMs = 29000L
        ),
        AyahItem(
            surahNumber = 114,
            ayahNumberInSurah = 6,
            ayahNumberInQuran = 6236,
            textUthmani = "مِنَ ٱلْجِنَّةِ وَٱلنَّاسِ",
            textIndopak = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            englishTranslation = "From among the jinn and mankind.\"",
            banglaTranslation = "জিনদের মধ্য থেকে এবং মানুষের মধ্য থেকে।",
            banglaTafsir = "শয়তান কেবল অদৃশ্য জিনদের মধ্যে নয়, মানুষের মধ্যেও শয়তানী চরিত্রের লোক থাকে।",
            words = listOf(WordItem(1, "مِنَ ٱلْجِنَّةِ", "From the jinn", "জিনদের মধ্য হতে"), WordItem(2, "وَٱلنَّاسِ", "and mankind", "ও মানুষের মধ্য হতে")),
            pageNumber = 604, juzNumber = 30, hizbNumber = 60, rukuNumber = 556,
            defaultStartMs = 29000L, defaultEndMs = 35000L
        )
    )

    // Surah Al-Kauthar (108) - 3 Ayahs
    val kautharAyahs = listOf(
        AyahItem(
            surahNumber = 108,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 6205,
            textUthmani = "إِنَّآ أَعْطَيْنَٰكَ ٱلْكَوْثَرَ",
            textIndopak = "اِنَّاۤ اَعْطَیْنٰكَ الْكَوْثَرَ",
            englishTranslation = "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
            banglaTranslation = "নিশ্চয় আমি আপনাকে কাওসার (অফুরন্ত কল্যাণ) দান করেছি।",
            banglaTafsir = "আল্লাহ তাআলা প্রিয়নবী (সা.)-কে জান্নাতে হাউজে কাওসার ও সীমাহীন নিয়ামত দান করেছেন।",
            words = listOf(WordItem(1, "إِنَّآ", "Indeed We", "নিশ্চয় আমরা"), WordItem(2, "أَعْطَيْنَٰكَ", "have granted you", "আপনাকে দিয়েছি"), WordItem(3, "ٱلْكَوْثَرَ", "al-Kawthar", "কাওসার")),
            pageNumber = 602, juzNumber = 30, hizbNumber = 60, rukuNumber = 550,
            defaultStartMs = 4000L, defaultEndMs = 8500L
        ),
        AyahItem(
            surahNumber = 108,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 6206,
            textUthmani = "فَصَلِّ لِرَبِّكَ وَٱنْحَرْ",
            textIndopak = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
            englishTranslation = "So pray to your Lord and sacrifice [to Him alone].",
            banglaTranslation = "অতএব আপনার রবের উদ্দেশ্যেই সালাত আদায় করুন এবং কুরবানি করুন।",
            banglaTafsir = "ইবাদত ও কুরবানি একমাত্র মহান আল্লাহর উদ্দেশ্যে নিবেদিত হতে হবে।",
            words = listOf(WordItem(1, "فَصَلِّ", "So pray", "সুতরাং সালাত পড়ুন"), WordItem(2, "لِرَبِّكَ", "to your Lord", "আপনার রবের জন্য"), WordItem(3, "وَٱنْحَرْ", "and sacrifice", "ও কুরবানি করুন")),
            pageNumber = 602, juzNumber = 30, hizbNumber = 60, rukuNumber = 550,
            defaultStartMs = 8500L, defaultEndMs = 13500L
        ),
        AyahItem(
            surahNumber = 108,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 6207,
            textUthmani = "إِنَّ شَانِئَكَ هُوَ ٱلْأَبْتَرُ",
            textIndopak = "اِنَّ شَانِئَكَ هُوَ الْاَبْتَرُ",
            englishTranslation = "Indeed, your enemy is the one cut off.",
            banglaTranslation = "নিশ্চয় আপনার শত্রুই তো নির্বংশ, শিকড়হীন।",
            banglaTafsir = "নবী (সা.)-এর সুখ্যাতি কিয়ামত পর্যন্ত অম্লান থাকবে, বিরোধীরাই ইতিহাসের আস্তাকুঁড়ে নিক্ষিপ্ত হবে।",
            words = listOf(WordItem(1, "إِنَّ شَانِئَكَ", "Indeed your enemy", "নিশ্চয় আপনার শত্রু"), WordItem(2, "هُوَ ٱلْأَبْتَرُ", "is cut off", "সেই তো নির্বংশ")),
            pageNumber = 602, juzNumber = 30, hizbNumber = 60, rukuNumber = 550,
            defaultStartMs = 13500L, defaultEndMs = 19000L
        )
    )

    // Surah Al-Asr (103) - 3 Ayahs
    val asrAyahs = listOf(
        AyahItem(
            surahNumber = 103,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 6177,
            textUthmani = "وَٱلْعَصْرِ",
            textIndopak = "وَالْعَصْرِ",
            englishTranslation = "By time,",
            banglaTranslation = "মহাকালের শপথ,",
            banglaTafsir = "সময়ের গুরুত্ব এবং মানবজীবনের সংক্ষিপ্ততার ওপর তাগিদ।",
            words = listOf(WordItem(1, "وَٱلْعَصْرِ", "By time", "কালের শপথ")),
            pageNumber = 601, juzNumber = 30, hizbNumber = 60, rukuNumber = 545,
            defaultStartMs = 3000L, defaultEndMs = 7000L
        ),
        AyahItem(
            surahNumber = 103,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 6178,
            textUthmani = "إِنَّ ٱلْإِنسَٰنَ لَفِى خُسْرٍ",
            textIndopak = "اِنَّ الْاِنْسَانَ لَفِیْ خُسْرٍ",
            englishTranslation = "Indeed, mankind is in loss,",
            banglaTranslation = "নিশ্চয় সমস্ত মানুষ চরম ক্ষতির মধ্যে নিমজ্জিত,",
            banglaTafsir = "ঈমান ও আমল ছাড়া প্রতিটি মানুষ পরকালীন ক্ষতির সম্মুখীন।",
            words = listOf(WordItem(1, "إِنَّ ٱلْإِنسَٰنَ", "Indeed mankind", "নিশ্চয় মানুষ"), WordItem(2, "لَفِى خُسْرٍ", "is in loss", "অবশ্যই ক্ষতির মধ্যে")),
            pageNumber = 601, juzNumber = 30, hizbNumber = 60, rukuNumber = 545,
            defaultStartMs = 7000L, defaultEndMs = 12000L
        ),
        AyahItem(
            surahNumber = 103,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 6179,
            textUthmani = "إِلَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّٰلِحَٰتِ وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ",
            textIndopak = "اِلَّا الَّذِیْنَ اٰمَنُوْا وَعَمِلُوا الصّٰلِحٰتِ وَتَوَاصَوْا بِالْحَقِّ ۬ وَتَوَاصَوْا بِالصَّبْرِ",
            englishTranslation = "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.",
            banglaTranslation = "তারা ব্যতীত যারা ঈমান এনেছে, সৎকাজ করেছে এবং পরস্পরকে সত্যের উপদেশ দিয়েছে ও ধৈর্যের উপদেশ দিয়েছে।",
            banglaTafsir = "নাজাতের চারটি মূল স্তম্ভ: ঈমান, নেক আমল, সত্যের দাওয়াত ও ধৈর্যের উপদেশ।",
            words = listOf(WordItem(1, "إِلَّا ٱلَّذِينَ", "Except those who", "তারা ছাড়া যারা"), WordItem(2, "ءَامَنُوا۟", "believed", "ঈমান এনেছে"), WordItem(3, "وَعَمِلُوا۟", "and did", "ও করেছে"), WordItem(4, "ٱلصَّٰلِحَٰتِ", "righteous deeds", "নেক আমল"), WordItem(5, "وَتَوَاصَوْا۟ بِٱلْحَقِّ", "and advised to truth", "সত্যের উপদেশ দিয়েছে"), WordItem(6, "وَتَوَاصَوْا۟ بِٱلصَّبْرِ", "and advised to patience", "ধৈর্যের উপদেশ দিয়েছে")),
            pageNumber = 601, juzNumber = 30, hizbNumber = 60, rukuNumber = 545,
            defaultStartMs = 12000L, defaultEndMs = 22000L
        )
    )

    // Surah Al-Kafirun (109) - 6 Ayahs
    val kafirunAyahs = listOf(
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 1, ayahNumberInQuran = 6208,
            textUthmani = "قُلْ يَٰٓأَيُّهَا ٱلْكَٰفِرُونَ", textIndopak = "قُلْ یٰۤاَیُّهَا الْكٰفِرُوْنَ",
            englishTranslation = "Say, \"O disbelievers,", banglaTranslation = "বলুন, হে কাফেরগণ!",
            banglaTafsir = "তাওহীদ ও শিরকের মধ্যে স্পষ্ট পার্থক্য ঘোষণা।",
            words = listOf(WordItem(1, "قُلْ", "Say", "বলুন"), WordItem(2, "يَٰٓأَيُّهَا ٱلْكَٰفِرُونَ", "O disbelievers", "হে অবিশ্বাসীগণ")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 4000L, defaultEndMs = 8500L
        ),
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 2, ayahNumberInQuran = 6209,
            textUthmani = "لَآ أَعْبُدُ مَا تَعْبُدُونَ", textIndopak = "لَاۤ اَعْبُدُ مَا تَعْبُدُوْنَ",
            englishTranslation = "I do not worship what you worship.", banglaTranslation = "আমি তাদের ইবাদত করি না যাদের তোমরা ইবাদত কর,",
            banglaTafsir = "কোনো প্রকার শিরকী পূজায় মুসলিমরা অংশগ্রহণ করতে পারে না।",
            words = listOf(WordItem(1, "لَآ أَعْبُدُ", "I do not worship", "আমি ইবাদত করি না"), WordItem(2, "مَا تَعْبُدُونَ", "what you worship", "তোমরা যার ইবাদত কর")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 8500L, defaultEndMs = 13500L
        ),
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 3, ayahNumberInQuran = 6210,
            textUthmani = "وَلَآ أَنتُمْ عَٰبِدُونَ مَآ أَعْبُدُ", textIndopak = "وَلَاۤ اَنْتُمْ عٰبِدُوْنَ مَاۤ اَعْبُدُ",
            englishTranslation = "Nor are you worshippers of what I worship.", banglaTranslation = "এবং তোমরাও তাঁর ইবাদতকারী নও যাঁর ইবাদত আমি করি,",
            banglaTafsir = "তাওহীদের বিশুদ্ধ ইবাদতের সাথে মূর্তিপূজার কোনো আপস নেই।",
            words = listOf(WordItem(1, "وَلَآ أَنتُمْ", "Nor you", "আর তোমরাও নও"), WordItem(2, "عَٰبِدُونَ", "worshippers", "ইবাদতকারী")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 13500L, defaultEndMs = 18500L
        ),
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 4, ayahNumberInQuran = 6211,
            textUthmani = "وَلَآ أَنَا۠ عَابِدٌ مَّا عَبَدتُّمْ", textIndopak = "وَلَاۤ اَنَا عَابِدٌ مَّا عَبَدْتُّمْ",
            englishTranslation = "Nor will I be a worshipper of what you worship.", banglaTranslation = "এবং আমি কখনই তাদের ইবাদতকারী হব না যাদের তোমরা ইবাদত করেছ,",
            banglaTafsir = "ভবিষ্যতেও শিরক বর্জনের অটুট অঙ্গীকার।",
            words = listOf(WordItem(1, "وَلَآ أَنَا۠", "Nor I", "আর না আমি"), WordItem(2, "عَابِدٌ", "worshipper", "ইবাদতকারী")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 18500L, defaultEndMs = 23500L
        ),
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 5, ayahNumberInQuran = 6212,
            textUthmani = "وَلَآ أَنتُمْ عَٰبِدُونَ مَآ أَعْبُدُ", textIndopak = "وَلَاۤ اَنْتُمْ عٰبِدُوْنَ مَاۤ اَعْبُدُ",
            englishTranslation = "Nor will you be worshippers of what I worship.", banglaTranslation = "এবং তোমরাও তাঁর ইবাদতকারী নও যাঁর ইবাদত আমি করি।",
            banglaTafsir = "কুফরির ওপর অবিচল থাকা ব্যক্তিদের পরিণতির সতর্কবার্তা।",
            words = listOf(WordItem(1, "وَلَآ أَنتُمْ", "Nor you", "আর তোমরাও নও"), WordItem(2, "عَٰبِدُونَ", "worshippers", "ইবাদতকারী")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 23500L, defaultEndMs = 28500L
        ),
        AyahItem(
            surahNumber = 109, ayahNumberInSurah = 6, ayahNumberInQuran = 6213,
            textUthmani = "لَكُمْ دِينُكُمْ وَلِىَ دِينِ", textIndopak = "لَكُمْ دِیْنُكُمْ وَلِیَ دِیْنِ",
            englishTranslation = "For you is your religion, and for me is my religion.\"", banglaTranslation = "তোমাদের জন্য তোমাদের দ্বীন এবং আমার জন্য আমার দ্বীন।",
            banglaTafsir = "ধর্ম পালনের স্বকীয়তা ও কুফরের সাথে ঈমানের সম্পূর্ণ সম্পর্কচ্ছেদ।",
            words = listOf(WordItem(1, "لَكُمْ دِينُكُمْ", "For you your religion", "তোমাদের জন্য তোমাদের ধর্ম"), WordItem(2, "وَلِىَ دِينِ", "and for me my religion", "ও আমার জন্য আমার ধর্ম")),
            pageNumber = 603, juzNumber = 30, hizbNumber = 60, rukuNumber = 551,
            defaultStartMs = 28500L, defaultEndMs = 34000L
        )
    )

    // Surah Al-Qadr (97) - 5 Ayahs
    val qadrAyahs = listOf(
        AyahItem(
            surahNumber = 97, ayahNumberInSurah = 1, ayahNumberInQuran = 6126,
            textUthmani = "إِنَّآ أَنزَلْنَٰهُ فِى لَيْلَةِ ٱلْقَدْرِ", textIndopak = "اِنَّاۤ اَنْزَلْنٰهُ فِیْ لَیْلَةِ الْقَدْرِ",
            englishTranslation = "Indeed, We sent the Qur'an down during the Night of Decree.", banglaTranslation = "নিশ্চয় আমি একে নাজিল করেছি লাইলাতুল কদরে (মর্যাদাময় রজনীতে)।",
            banglaTafsir = "কুরআনুল কারীম অবতীর্ণ হওয়ার মহিমান্বিত রাতের গুরুত্ব।",
            words = listOf(WordItem(1, "إِنَّآ", "Indeed We", "নিশ্চয় আমরা"), WordItem(2, "أَنزَلْنَٰهُ", "sent it down", "তা নাজিল করেছি"), WordItem(3, "فِى لَيْلَةِ ٱلْقَدْرِ", "in Night of Decree", "কদরের রাতে")),
            pageNumber = 598, juzNumber = 30, hizbNumber = 60, rukuNumber = 538,
            defaultStartMs = 4000L, defaultEndMs = 9000L
        ),
        AyahItem(
            surahNumber = 97, ayahNumberInSurah = 2, ayahNumberInQuran = 6127,
            textUthmani = "وَمَآ أَدْرَىٰكَ مَا لَيْلَةُ ٱلْقَدْرِ", textIndopak = "وَمَاۤ اَدْرٰىكَ مَا لَیْلَةُ الْقَدْرِ",
            englishTranslation = "And what can make you know what is the Night of Decree?", banglaTranslation = "আর আপনি কি জানেন লাইলাতুল কদর কী?",
            banglaTafsir = "এই রাতের সম্মান ও ফজিলত মানুষের কল্পনারও অতীত।",
            words = listOf(WordItem(1, "وَمَآ أَدْرَىٰكَ", "And what made you know", "আর কিসে আপনাকে জানাবে"), WordItem(2, "مَا لَيْلَةُ ٱلْقَدْرِ", "what Night of Decree is", "কদরের রাত কী")),
            pageNumber = 598, juzNumber = 30, hizbNumber = 60, rukuNumber = 538,
            defaultStartMs = 9000L, defaultEndMs = 14000L
        ),
        AyahItem(
            surahNumber = 97, ayahNumberInSurah = 3, ayahNumberInQuran = 6128,
            textUthmani = "لَيْلَةُ ٱلْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ", textIndopak = "لَیْلَةُ الْقَدْرِ خَیْرٌ مِّنْ اَلْفِ شَهْرٍ",
            englishTranslation = "The Night of Decree is better than a thousand months.", banglaTranslation = "লাইলাতুল কদর এক হাজার মাসের চেয়েও শ্রেষ্ঠ।",
            banglaTafsir = "এই রাতের এক মুহূর্তের ইবাদত হাজার মাসের (প্রায় ৮৩ বছর) চেয়ে অধিক সওয়াবের।",
            words = listOf(WordItem(1, "لَيْلَةُ ٱلْقَدْرِ", "Night of Decree", "কদরের রাত"), WordItem(2, "خَيْرٌ", "is better", "উত্তম"), WordItem(3, "مِّنْ أَلْفِ شَهْرٍ", "than 1000 months", "হাজার মাস হতে")),
            pageNumber = 598, juzNumber = 30, hizbNumber = 60, rukuNumber = 538,
            defaultStartMs = 14000L, defaultEndMs = 20000L
        ),
        AyahItem(
            surahNumber = 97, ayahNumberInSurah = 4, ayahNumberInQuran = 6129,
            textUthmani = "تَنَزَّلُ ٱلْمَلَٰٓئِكَةُ وَٱلرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ", textIndopak = "تَنَزَّلُ الْمَلٰٓئِكَةُ وَالرُّوْحُ فِیْهَا بِاِذْنِ رَبِّهِمْ ۚ مِنْ كُلِّ اَمْرٍ",
            englishTranslation = "The angels and the Spirit descend therein by permission of their Lord for every matter.", banglaTranslation = "এ রাতে ফেরেশতাগণ ও জিবরীল (আ.) তাদের রবের অনুমতিক্রমে সকল কল্যাণময় সিদ্ধান্ত নিয়ে অবতীর্ণ হন।",
            banglaTafsir = "রহমত, মাগফিরাত ও বরকত নিয়ে ফেরেশতাদের বিপুল আগমন ঘটে।",
            words = listOf(WordItem(1, "تَنَزَّلُ ٱلْمَلَٰٓئِكَةُ", "Angels descend", "ফেরেশতারা অবতীর্ণ হন"), WordItem(2, "وَٱلرُّوحُ", "and the Spirit", "ও জিবরীল (আ.)"), WordItem(3, "بِإِذْنِ رَبِّهِم", "by permission of Lord", "রবের অনুমতিক্রমে")),
            pageNumber = 598, juzNumber = 30, hizbNumber = 60, rukuNumber = 538,
            defaultStartMs = 20000L, defaultEndMs = 28000L
        ),
        AyahItem(
            surahNumber = 97, ayahNumberInSurah = 5, ayahNumberInQuran = 6130,
            textUthmani = "سَلَٰمٌ هِىَ حَتَّىٰ مَطْلَعِ ٱلْفَجْرِ", textIndopak = "سَلٰمٌ ۫ هِیَ حَتّٰى مَطْلَعِ الْفَجْرِ",
            englishTranslation = "Peace it is until the emergence of dawn.", banglaTranslation = "শান্তিময় এ রজনী, ফজর উদিত হওয়া পর্যন্ত।",
            banglaTafsir = "সন্ধ্যা থেকে শুরু করে সুবহে সাদিক পর্যন্ত কেবলই নিরাপত্তা ও শান্তি।",
            words = listOf(WordItem(1, "سَلَٰمٌ هِىَ", "Peace it is", "তা কেবলই শান্তি"), WordItem(2, "حَتَّىٰ", "until", "পর্যন্ত"), WordItem(3, "مَطْلَعِ ٱلْفَجْرِ", "emergence of dawn", "ফজরের উদয়")),
            pageNumber = 598, juzNumber = 30, hizbNumber = 60, rukuNumber = 538,
            defaultStartMs = 28000L, defaultEndMs = 35000L
        )
    )

    // Surah Ad-Duha (93) - 11 Ayahs
    val duhaAyahs = listOf(
        AyahItem(surahNumber = 93, ayahNumberInSurah = 1, ayahNumberInQuran = 6088, textUthmani = "وَٱلضُّحَىٰ", textIndopak = "وَالضُّحٰى", englishTranslation = "By the morning brightness", banglaTranslation = "শপথ পূর্বাহ্নের রৌদ্রোজ্জ্বল আলোর,", banglaTafsir = "দিনের প্রারম্ভিক আলোর মহিমা।", words = listOf(WordItem(1, "وَٱلضُّحَىٰ", "By morning", "পূর্বাহ্ণের শপথ")), pageNumber = 596, juzNumber = 30, hizbNumber = 60, rukuNumber = 534, defaultStartMs = 3000L, defaultEndMs = 7000L),
        AyahItem(surahNumber = 93, ayahNumberInSurah = 2, ayahNumberInQuran = 6089, textUthmani = "وَٱلَّيْلِ إِذَا سَجَىٰ", textIndopak = "وَالَّیْلِ اِذَا سَجٰى", englishTranslation = "And [by] the night when it covers with darkness,", banglaTranslation = "এবং শপথ রাতের, যখন তা নিস্তব্ধ হয়ে যায়,", banglaTafsir = "রাত্রির প্রশান্তির শপথ।", words = listOf(WordItem(1, "وَٱلَّيْلِ", "And the night", "ও রাতের"), WordItem(2, "إِذَا سَجَىٰ", "when dark", "যখন তা নিস্তব্ধ হয়")), pageNumber = 596, juzNumber = 30, hizbNumber = 60, rukuNumber = 534, defaultStartMs = 7000L, defaultEndMs = 11000L),
        AyahItem(surahNumber = 93, ayahNumberInSurah = 3, ayahNumberInQuran = 6090, textUthmani = "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", textIndopak = "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلٰى", englishTranslation = "Your Lord has not taken leave of you, [O Muhammad], nor has He detested [you].", banglaTranslation = "আপনার রব আপনাকে পরিত্যাগ করেননি এবং অপ্রসন্নও হননি।", banglaTafsir = "নবী (সা.)-এর প্রতি আল্লাহর অপার ভালোবাসার সান্ত্বনা।", words = listOf(WordItem(1, "مَا وَدَّعَكَ", "Not abandoned you", "পরিত্যাগ করেননি"), WordItem(2, "رَبُّكَ", "your Lord", "আপনার রব"), WordItem(3, "وَمَا قَلَىٰ", "nor hated", "ও অসন্তুষ্ট হননি")), pageNumber = 596, juzNumber = 30, hizbNumber = 60, rukuNumber = 534, defaultStartMs = 11000L, defaultEndMs = 17000L),
        AyahItem(surahNumber = 93, ayahNumberInSurah = 4, ayahNumberInQuran = 6091, textUthmani = "وَلَلْـَٔاخِرَةُ خَيْرٌ لَّكَ مِنَ ٱلْأُولَىٰ", textIndopak = "وَلَلْاٰخِرَةُ خَیْرٌ لَّكَ مِنَ الْاُوْلٰى", englishTranslation = "And the Hereafter is better for you than the first [life].", banglaTranslation = "আর আপনার জন্য পরকাল ইহকাল অপেক্ষা বহুগুণ শ্রেয়।", banglaTafsir = "আখেরাতে অনন্ত মর্যাদা ও প্রতিদানের নিশ্চয়তা।", words = listOf(WordItem(1, "وَلَلْـَٔاخِرَةُ", "And the Hereafter", "আর আখেরাত"), WordItem(2, "خَيْرٌ لَّكَ", "better for you", "উত্তম আপনার জন্য"), WordItem(3, "مِنَ ٱلْأُولَىٰ", "than first life", "ইহকাল থেকে")), pageNumber = 596, juzNumber = 30, hizbNumber = 60, rukuNumber = 534, defaultStartMs = 17000L, defaultEndMs = 23000L),
        AyahItem(surahNumber = 93, ayahNumberInSurah = 5, ayahNumberInQuran = 6092, textUthmani = "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", textIndopak = "وَلَسَوْفَ یُعْطِیْكَ رَبُّكَ فَتَرْضٰى", englishTranslation = "And your Lord is going to give you, and you will be satisfied.", banglaTranslation = "আর অচিরেই আপনার রব আপনাকে এত দেবেন যে আপনি সন্তুষ্ট হয়ে যাবেন।", banglaTafsir = "শাফায়াতে কুবরা ও জান্নাতের সর্বোচ্চ মর্যাদার সুসংবাদ।", words = listOf(WordItem(1, "وَلَسَوْفَ يُعْطِيكَ", "And soon will give you", "অচিরেই আপনাকে দেবেন"), WordItem(2, "رَبُّكَ", "your Lord", "আপনার রব"), WordItem(3, "فَتَرْضَىٰ", "so you will be pleased", "ফলে আপনি সন্তুষ্ট হবেন")), pageNumber = 596, juzNumber = 30, hizbNumber = 60, rukuNumber = 534, defaultStartMs = 23000L, defaultEndMs = 30000L)
    )
}
