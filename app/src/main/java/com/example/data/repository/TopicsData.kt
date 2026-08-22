package com.example.data.repository

import com.example.data.model.TopicItem
import com.example.data.model.TopicVerseRef

object TopicsData {
    val topicsList: List<TopicItem> = listOf(
        TopicItem(
            id = "allah",
            title = "Allah ﷻ",
            subTitle = "Attributes, Oneness, Names of Allah",
            iconName = "allah",
            versesCount = 38,
            verses = listOf(
                TopicVerseRef(2, 255, "Ayatul Kursi (Throne Verse)", "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ", "আল্লাহ! তিনি ছাড়া কোনো সত্য ইলাহ নেই; তিনি চিরঞ্জীব, সর্বসত্তার ধারক।"),
                TopicVerseRef(112, 1, "Tawheed & Oneness", "قُلْ هُوَ ٱللَّهُ أَحَدٌ", "বলুন, তিনিই আল্লাহ, একক।"),
                TopicVerseRef(59, 23, "Asma ul Husna", "هُوَ ٱللَّهُ ٱلَّذِى لَآ إِلَٰهَ إِلَّا هُوَ ٱلْمَلِكُ ٱلْقُدُّوسُ", "তিনিই আল্লাহ, যিনি ব্যতীত কোনো ইলাহ নেই, তিনি রাজাধিরাজ, পরম পবিত্র।")
            )
        ),
        TopicItem(
            id = "aqidah",
            title = "Aqidah (Belief)",
            subTitle = "Faith in Angels, Books, Prophets, Destiny",
            iconName = "aqidah",
            versesCount = 26,
            verses = listOf(
                TopicVerseRef(2, 285, "Articles of Faith", "ءَامَنَ ٱلرَّسُولُ بِمَآ أُنزِلَ إِلَيْهِ مِن رَّبِّهِۦ وَٱلْمُؤْمِنُونَ", "রাসূল তাঁর প্রতিপালকের পক্ষ থেকে যা অবতীর্ণ হয়েছে তার ওপর ঈমান এনেছেন এবং মুমিনগণও।"),
                TopicVerseRef(4, 136, "Command to Believe", "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوٓا۟ ءَامِنُوا۟ بِٱللَّهِ وَرَسُولِهِۦ", "হে ঈমানদারগণ! তোমরা আল্লাহ, তাঁর রাসূল ও অবতীর্ণ কিতাবের ওপর ঈমান আনো।")
            )
        ),
        TopicItem(
            id = "ibadah",
            title = "Ibadah (worship of Allah)",
            subTitle = "Salah, Sawm, Zakat, Hajj, Dhikr",
            iconName = "ibadah",
            versesCount = 45,
            verses = listOf(
                TopicVerseRef(2, 43, "Establish Salah & Zakah", "وَأَقِيمُوا۟ ٱلصَّلَوٰةَ وَءَاتُوا۟ ٱلزَّكَوٰةَ وَٱرْكَعُوا۟ مَعَ ٱلرَّٰكِعِينَ", "আর তোমরা সালাত কায়েম কর এবং যাকাত প্রদান কর এবং রুকুকারীদের সাথে রুকু কর।"),
                TopicVerseRef(2, 183, "Fasting in Ramadan", "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ كُتِبَ عَلَيْكُمُ ٱلصِّيَامُ", "হে ঈমানদারগণ! তোমাদের ওপর সিয়াম ফরজ করা হয়েছে যেমন তোমাদের পূর্ববর্তীদের ওপর করা হয়েছিল।")
            )
        ),
        TopicItem(
            id = "akhirah",
            title = "Akhirah (Afterlife)",
            subTitle = "Day of Judgment, Jannah, Jahannam",
            iconName = "akhirah",
            versesCount = 32,
            verses = listOf(
                TopicVerseRef(75, 1, "The Resurrection", "لَآ أُقْسِمُ بِيَوْمِ ٱلْقِيَٰمَةِ", "আমি শপথ করছি কিয়ামতের দিবসের।"),
                TopicVerseRef(55, 46, "Two Gardens of Jannah", "وَلِمَنْ خَافَ مَقَامَ رَبِّهِۦ جَنَّتَانِ", "আর যে ব্যক্তি তার প্রতিপালকের সামনে দাঁড়ানোকে ভয় করে, তার জন্য রয়েছে দুটি উদ্যান।")
            )
        ),
        TopicItem(
            id = "etiquette",
            title = "Etiquette and manners",
            subTitle = "Truthfulness, Humility, Respecting Parents",
            iconName = "etiquette",
            versesCount = 29,
            verses = listOf(
                TopicVerseRef(17, 23, "Kindness to Parents", "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوٓا۟ إِلَّآ إِيَّاهُ وَبِٱلْوَٰلِدَيْنِ إِحْسَٰنًا", "আর তোমার রব আদেশ করেছেন যে, তোমরা তিনি ছাড়া অন্য কারো ইবাদত করবে না এবং পিতা-মাতার সাথে সদ্ব্যবহার করবে।"),
                TopicVerseRef(49, 12, "Avoid Backbiting", "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ ٱجْتَنِبُوا۟ كَثِيرًا مِّنَ ٱلظَّنِّ", "হে ঈমানদারগণ! তোমরা অধিকাংশ অনুমান থেকে দূরে থাকো, নিশ্চয় কিছু অনুমান পাপ।")
            )
        ),
        TopicItem(
            id = "history",
            title = "History & Biographies",
            subTitle = "Stories of Prophets & Past Civilizations",
            iconName = "history",
            versesCount = 52,
            verses = listOf(
                TopicVerseRef(12, 3, "Story of Yusuf (AS)", "نَحْنُ نَقُصُّ عَلَيْكَ أَحْسَنَ ٱلْقَصَصِ", "আমরা আপনার নিকট সর্বোত্তম কাহিনী বর্ণনা করছি।"),
                TopicVerseRef(18, 9, "People of the Cave", "أَمْ حَسِبْتَ أَنَّ أَصْحَٰبَ ٱلْكَهْفِ وَٱلرَّقِيمِ كَانُوا۟ مِنْ ءَايَٰتِنَا عَجَبًا", "তুমি কি মনে কর যে, গুহা ও রাকিমের অধিবাসীরা আমাদের নিদর্শনাবলীর মধ্যে এক বিস্ময়কর ছিল?")
            )
        ),
        TopicItem(
            id = "muamalat",
            title = "Mu'amalat (dealings)",
            subTitle = "Trade, Contracts, Prohibition of Riba",
            iconName = "muamalat",
            versesCount = 20,
            verses = listOf(
                TopicVerseRef(2, 275, "Trade vs Usury (Riba)", "وَأَحَلَّ ٱللَّهُ ٱلْبَيْعَ وَحَرَّمَ ٱلرِّبَوٰا۟", "আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন।"),
                TopicVerseRef(2, 282, "Contracts & Record Keeping", "يَٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوٓا۟ إِذَا تَدَايَنتُم بِدَيْنٍ فَٱكْتُبُوهُ", "হে মুমিনগণ! যখন তোমরা নির্দিষ্ট মেয়াদের জন্য ঋণ আদান-প্রদান কর তখন তা লিখে রাখ।")
            )
        ),
        TopicItem(
            id = "family",
            title = "Family",
            subTitle = "Marriage, Children, Inheritance",
            iconName = "family",
            versesCount = 24,
            verses = listOf(
                TopicVerseRef(30, 21, "Love & Mercy in Marriage", "وَمِنْ ءَايَٰتِهِۦٓ أَنْ خَلَقَ لَكُم مِّنْ أَنفُسِكُمْ أَزْوَٰجًا لِّتَسْكُنُوٓا۟ إِلَيْهَا", "আর তাঁর নিদর্শনাবলীর মধ্যে রয়েছে যে, তিনি তোমাদের জন্য তোমাদের মধ্য থেকে সৃষ্টি করেছেন সঙ্গিনী যাতে তোমরা তাদের নিকট প্রশান্তি পাও।")
            )
        ),
        TopicItem(
            id = "politics",
            title = "Politics & Justice",
            subTitle = "Leadership, Rule of Law, Consultation",
            iconName = "politics",
            versesCount = 18,
            verses = listOf(
                TopicVerseRef(4, 58, "Judge with Justice", "إِنَّ ٱللَّهَ يَأْمُرُكُمْ أَن تُؤَدُّوا۟ ٱلْأَمَٰنَٰتِ إِلَىٰٓ أَهْلِهَا", "নিশ্চয় আল্লাহ তোমাদের নির্দেশ দিচ্ছেন যে, তোমরা আমানতসমূহ তার প্রাপকদের নিকট অর্পণ করবে এবং যখন মানুষের মাঝে বিচার করবে তখন ন্যায়পরায়ণতার সাথে বিচার করবে।")
            )
        ),
        TopicItem(
            id = "duas",
            title = "Duas in the Quran",
            subTitle = "Rabbana Duas, Protection & Forgiveness",
            iconName = "duas",
            versesCount = 40,
            verses = listOf(
                TopicVerseRef(2, 201, "Dua for Good in Both Worlds", "رَبَّنَآ ءَاتِنَا فِى ٱلدُّنْيَا حَسَنَةً وَفِى ٱلْـَٔاخِرَةِ حَسَنَةً وَقِنَا عَذَابَ ٱلنَّارِ", "হে আমাদের প্রতিপালক! আমাদের দুনিয়াতে কল্যাণ দিন এবং আখেরাতেও কল্যাণ দিন এবং আমাদের জাহান্নামের আযাব থেকে রক্ষা করুন।"),
                TopicVerseRef(3, 8, "Dua for Firm Faith", "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا", "হে আমাদের রব! সরল পথ দেখানোর পর আমাদের অন্তরকে বক্র করবেন না।")
            )
        )
    )
}
