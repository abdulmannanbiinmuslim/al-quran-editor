package com.example.data.repository

import com.example.data.model.AyahItem
import com.example.data.model.SurahItem
import com.example.data.model.WordItem

data class JuzInfo(val number: Int, val startSurah: Int, val startAyah: Int, val arabicName: String, val startSurahName: String)
data class HizbInfo(val number: Int, val quarter: String, val startSurah: Int, val startAyah: Int, val arabicName: String, val startSurahName: String)
data class PageInfo(val pageNumber: Int, val startSurah: Int, val startAyah: Int, val startSurahName: String, val arabicSnippet: String)
data class RukuInfo(val rukuNumber: Int, val startSurah: Int, val startAyah: Int, val startSurahName: String, val arabicSnippet: String)

object QuranData {

    val surahs: List<SurahItem> = listOf(
        SurahItem(1, "Al-Fatihah", "الفاتحة", "The Opening", "সূচনা", 7, "Meccan", 1, 1, 1, 1),
        SurahItem(2, "Al-Baqarah", "البقرة", "The Cow", "বকনা বাছুর", 286, "Medinan", 2, 1, 1, 2),
        SurahItem(3, "Ali 'Imran", "آل عمران", "Family of Imran", "ইমরানের পরিবার", 200, "Medinan", 50, 3, 6, 42),
        SurahItem(4, "An-Nisa", "النساء", "The Women", "নারী", 176, "Medinan", 77, 4, 8, 62),
        SurahItem(5, "Al-Ma'idah", "المائدة", "The Table Spread", "খাদ্য পরিবেশিত টেবিল", 120, "Medinan", 106, 6, 11, 86),
        SurahItem(6, "Al-An'am", "الأنعام", "The Cattle", "গৃহপালিত পশু", 165, "Meccan", 128, 7, 13, 102),
        SurahItem(7, "Al-A'raf", "الأعراف", "The Heights", "উঁচু স্থানসমূহ", 206, "Meccan", 151, 8, 16, 122),
        SurahItem(8, "Al-Anfal", "الأنفال", "The Spoils of War", "যুদ্ধলব্ধ ধন-সম্পদ", 75, "Medinan", 177, 9, 19, 146),
        SurahItem(9, "At-Tawbah", "التوبة", "The Repentance", "অনুশোচনা", 129, "Medinan", 187, 10, 20, 156),
        SurahItem(10, "Yunus", "يونس", "Jonah", "ইউনুস", 109, "Meccan", 208, 11, 21, 172),
        SurahItem(11, "Hud", "هود", "Hud", "হুদ", 123, "Meccan", 221, 11, 23, 183),
        SurahItem(12, "Yusuf", "يوسف", "Joseph", "ইউসুফ", 111, "Meccan", 235, 12, 24, 193),
        SurahItem(13, "Ar-Ra'd", "الرعد", "The Thunder", "বজ্রনাদ", 43, "Medinan", 249, 13, 26, 205),
        SurahItem(14, "Ibrahim", "إبراهيم", "Abraham", "ইবরাহীম", 52, "Meccan", 255, 13, 26, 211),
        SurahItem(15, "Al-Hijr", "الحجر", "The Rocky Tract", "পাথুরে ভূভাগ", 99, "Meccan", 262, 14, 27, 218),
        SurahItem(16, "An-Nahl", "النحل", "The Bee", "মৌমাছি", 128, "Meccan", 267, 14, 28, 224),
        SurahItem(17, "Al-Isra", "الإسراء", "The Night Journey", "ইসরার রাত্রি ভ্রমণ", 111, "Meccan", 282, 15, 29, 240),
        SurahItem(18, "Al-Kahf", "الكهف", "The Cave", "গুহা", 110, "Meccan", 293, 15, 30, 252),
        SurahItem(19, "Maryam", "مريم", "Mary", "মারইয়াম", 98, "Meccan", 305, 16, 31, 264),
        SurahItem(20, "Ta-Ha", "طه", "Ta-Ha", "ত্বোয়া-হা", 135, "Meccan", 312, 16, 32, 270),
        SurahItem(21, "Al-Anbiya", "الأنبياء", "The Prophets", "নবীগণ", 112, "Meccan", 322, 17, 33, 277),
        SurahItem(22, "Al-Hajj", "الحج", "The Pilgrimage", "হজ্জ", 78, "Medinan", 332, 17, 34, 284),
        SurahItem(23, "Al-Mu'minun", "المؤمنون", "The Believers", "বিশ্বাসীগণ", 118, "Meccan", 342, 18, 35, 294),
        SurahItem(24, "An-Nur", "النور", "The Light", "আলো", 64, "Medinan", 350, 18, 36, 300),
        SurahItem(25, "Al-Furqan", "الفرقان", "The Criterion", "সত্য-মিথ্যার পার্থক্যকারী", 77, "Meccan", 359, 18, 37, 309),
        SurahItem(26, "Ash-Shu'ara", "الشعراء", "The Poets", "কবিগণ", 227, "Meccan", 367, 19, 38, 315),
        SurahItem(27, "An-Naml", "النمل", "The Ant", "পিপীলিকা", 93, "Meccan", 377, 19, 39, 326),
        SurahItem(28, "Al-Qasas", "القصص", "The Stories", "কাহিনীর বিবরণ", 88, "Meccan", 385, 20, 40, 335),
        SurahItem(29, "Al-'Ankabut", "العنكبوت", "The Spider", "মাকড়সা", 69, "Meccan", 396, 20, 41, 344),
        SurahItem(30, "Ar-Rum", "الروم", "The Romans", "রোমবাসী", 60, "Meccan", 404, 21, 42, 351),
        SurahItem(31, "Luqman", "لقمان", "Luqman", "লুকমান", 34, "Meccan", 411, 21, 42, 357),
        SurahItem(32, "As-Sajdah", "السجدة", "The Prostration", "সিজদাহ", 30, "Meccan", 415, 21, 43, 361),
        SurahItem(33, "Al-Ahzab", "الأحزاب", "The Combined Forces", "জোটবদ্ধ দলসমূহ", 73, "Medinan", 418, 21, 43, 364),
        SurahItem(34, "Saba", "سبإ", "Sheba", "সাবা জাতি", 54, "Meccan", 428, 22, 44, 373),
        SurahItem(35, "Fatir", "فاطر", "The Originator", "সৃষ্টিকর্তা", 45, "Meccan", 434, 22, 45, 379),
        SurahItem(36, "Ya-Sin", "يس", "Ya-Sin", "ইয়া-সীন", 83, "Meccan", 440, 22, 46, 384),
        SurahItem(37, "As-Saffat", "الصافات", "Those Who Set The Ranks", "সারিবদ্ধভাবে দাঁড়ানো দল", 182, "Meccan", 446, 23, 47, 389),
        SurahItem(38, "Sad", "ص", "Sad", "সোয়াদ", 88, "Meccan", 453, 23, 48, 394),
        SurahItem(39, "Az-Zumar", "الزمر", "The Troops", "দলসমূহ", 75, "Meccan", 458, 23, 48, 399),
        SurahItem(40, "Ghafir", "غافر", "The Forgiver", "ক্ষমাকারী", 85, "Meccan", 467, 24, 49, 407),
        SurahItem(41, "Fussilat", "فصلت", "Explained in Detail", "সুস্পষ্ট বিবরণ", 54, "Meccan", 477, 24, 50, 416),
        SurahItem(42, "Ash-Shura", "الشورى", "The Consultation", "পরামর্শ", 53, "Meccan", 483, 25, 51, 422),
        SurahItem(43, "Az-Zukhruf", "الزخرف", "The Gold Adornments", "সোনার অলংকার", 89, "Meccan", 489, 25, 52, 427),
        SurahItem(44, "Ad-Dukhan", "الدخان", "The Smoke", "ধোঁয়া", 59, "Meccan", 496, 25, 52, 434),
        SurahItem(45, "Al-Jathiyah", "الجاثية", "The Crouching", "নতজানু", 37, "Meccan", 499, 25, 53, 437),
        SurahItem(46, "Al-Ahqaf", "الأحقاف", "The Wind-Curved Sandhills", "বালুকাময় পাহাড়", 35, "Meccan", 502, 26, 53, 441),
        SurahItem(47, "Muhammad", "محمد", "Muhammad", "মুহাম্মদ", 38, "Medinan", 507, 26, 54, 446),
        SurahItem(48, "Al-Fath", "الفتح", "The Victory", "বিজয়", 29, "Medinan", 511, 26, 54, 450),
        SurahItem(49, "Al-Hujurat", "الحجرات", "The Rooms", "কক্ষসমূহ", 18, "Medinan", 515, 26, 55, 454),
        SurahItem(50, "Qaf", "ق", "Qaf", "ক্বাফ", 45, "Meccan", 518, 26, 55, 456),
        SurahItem(51, "Adh-Dhariyat", "الذاريات", "The Winnowing Winds", "বিক্ষিপ্তকারী বাতাস", 60, "Meccan", 520, 26, 56, 459),
        SurahItem(52, "At-Tur", "الطور", "The Mount", "তূর পর্বত", 49, "Meccan", 523, 27, 56, 462),
        SurahItem(53, "An-Najm", "النجم", "The Star", "তারা", 62, "Meccan", 526, 27, 56, 464),
        SurahItem(54, "Al-Qamar", "القمر", "The Moon", "চাঁদ", 55, "Meccan", 528, 27, 57, 467),
        SurahItem(55, "Ar-Rahman", "الرحمن", "The Beneficent", "পরম করুণাময়", 78, "Medinan", 531, 27, 57, 470),
        SurahItem(56, "Al-Waqi'ah", "الواقعة", "The Inevitable", "নিশ্চিত ঘটনা", 96, "Meccan", 534, 27, 58, 473),
        SurahItem(57, "Al-Hadid", "الحديد", "The Iron", "লোহা", 29, "Medinan", 537, 27, 58, 477),
        SurahItem(58, "Al-Mujadila", "المجادلة", "The Pleading Woman", "বিতর্ককারিণী", 22, "Medinan", 542, 28, 59, 481),
        SurahItem(59, "Al-Hashr", "الحشر", "The Exile", "সমাবেশ", 24, "Medinan", 545, 28, 59, 484),
        SurahItem(60, "Al-Mumtahanah", "الممتحنة", "She That Is To Be Examined", "পরীক্ষিতা নারী", 13, "Medinan", 549, 28, 60, 487),
        SurahItem(61, "As-Saff", "الصف", "The Ranks", "সারিবদ্ধ সৈন্যদল", 14, "Medinan", 551, 28, 60, 489),
        SurahItem(62, "Al-Jumu'ah", "الجمعة", "The Congregation, Friday", "শুক্রবার / জুমা", 11, "Medinan", 553, 28, 60, 491),
        SurahItem(63, "Al-Munafiqun", "المنافقون", "The Hypocrites", "কপট বিশ্বাসীগণ", 11, "Medinan", 554, 28, 60, 493),
        SurahItem(64, "At-Taghabun", "التغابن", "The Mutual Disillusion", "লাভ-ক্ষতির দিন", 18, "Medinan", 556, 28, 60, 495),
        SurahItem(65, "At-Talaq", "الطلاق", "The Divorce", "তালাক", 12, "Medinan", 558, 28, 60, 497),
        SurahItem(66, "At-Tahrim", "التحريم", "The Prohibition", "নিষিদ্ধকরণ", 12, "Medinan", 560, 28, 60, 499),
        SurahItem(67, "Al-Mulk", "الملك", "The Sovereignty", "সার্বভৌম কর্তৃত্ব", 30, "Meccan", 562, 29, 60, 501),
        SurahItem(68, "Al-Qalam", "القلم", "The Pen", "কলম", 52, "Meccan", 564, 29, 60, 503),
        SurahItem(69, "Al-Haqqah", "الحاقة", "The Reality", "সুনিশ্চিত সত্য", 52, "Meccan", 566, 29, 60, 505),
        SurahItem(70, "Al-Ma'arij", "المعارج", "The Ascending Stairways", "উন্নয়নের সোপান", 44, "Meccan", 568, 29, 60, 507),
        SurahItem(71, "Nuh", "نوح", "Noah", "নূহ", 28, "Meccan", 570, 29, 60, 509),
        SurahItem(72, "Al-Jinn", "الجن", "The Jinn", "জিন জাতি", 28, "Meccan", 572, 29, 60, 511),
        SurahItem(73, "Al-Muzzammil", "المزمل", "The Enshrouded One", "বস্ত্রাবৃত", 20, "Meccan", 574, 29, 60, 513),
        SurahItem(74, "Al-Muddaththir", "المدثر", "The Cloaked One", "চাদরাবৃত", 56, "Meccan", 575, 29, 60, 515),
        SurahItem(75, "Al-Qiyamah", "القيامة", "The Resurrection", "কেয়ামত / পুনরুত্থান", 40, "Meccan", 577, 29, 60, 517),
        SurahItem(76, "Al-Insan", "الإنسان", "The Man", "মানবজাতি", 31, "Medinan", 578, 29, 60, 519),
        SurahItem(77, "Al-Mursalat", "المرسلات", "The Emissaries", "প্রেরিত বাতাসসমূহ", 50, "Meccan", 580, 29, 60, 521),
        SurahItem(78, "An-Naba", "النبإ", "The Tidings", "মহা সংবাদ", 40, "Meccan", 582, 30, 60, 523),
        SurahItem(79, "An-Nazi'at", "النازعات", "Those Who Drag Forth", "উৎপাটনকারী ফেরেশতা", 46, "Meccan", 583, 30, 60, 525),
        SurahItem(80, "'Abasa", "عبس", "He Frowned", "তিনি ভ্রূকুটি করলেন", 42, "Meccan", 585, 30, 60, 527),
        SurahItem(81, "At-Takwir", "التكوير", "The Overthrowing", "অন্ধকারাচ্ছন্নকরণ", 29, "Meccan", 586, 30, 60, 528),
        SurahItem(82, "Al-Infitar", "الانفطار", "The Cleaving", "বিদীর্ণ হওয়া", 19, "Meccan", 587, 30, 60, 529),
        SurahItem(83, "Al-Mutaffifin", "المطففين", "The Defrauding", "ওজনে কম দানকারী", 36, "Meccan", 587, 30, 60, 530),
        SurahItem(84, "Al-Inshiqaq", "الانشقاق", "The Splitting Open", "খন্ড-বিখন্ড হওয়া", 25, "Meccan", 589, 30, 60, 531),
        SurahItem(85, "Al-Buruj", "البروج", "The Mansions of the Stars", "নক্ষত্রপুঞ্জ", 22, "Meccan", 590, 30, 60, 532),
        SurahItem(86, "At-Tariq", "الطارق", "The Morning Star", "রাতের আগমনকারী", 17, "Meccan", 591, 30, 60, 533),
        SurahItem(87, "Al-A'la", "الأعلى", "The Most High", "সর্বোচ্চ সত্তা", 19, "Meccan", 591, 30, 60, 534),
        SurahItem(88, "Al-Ghashiyah", "الغاشية", "The Overwhelming", "আচ্ছন্নকারী বিপর্যয়", 26, "Meccan", 592, 30, 60, 535),
        SurahItem(89, "Al-Fajr", "الفجر", "The Dawn", "ঊষা / ভোরবেলা", 30, "Meccan", 593, 30, 60, 536),
        SurahItem(90, "Al-Balad", "البلد", "The City", "নগরী", 20, "Meccan", 594, 30, 60, 537),
        SurahItem(91, "Ash-Shams", "الشمس", "The Sun", "সূর্য", 15, "Meccan", 595, 30, 60, 538),
        SurahItem(92, "Al-Layl", "الليل", "The Night", "রাত", 21, "Meccan", 595, 30, 60, 539),
        SurahItem(93, "Ad-Duhaa", "الضحى", "The Morning Hours", "পূর্বাহ্ণ", 11, "Meccan", 596, 30, 60, 540),
        SurahItem(94, "Ash-Sharh", "الشرح", "The Relief", "বক্ষ প্রশস্তকরণ", 8, "Meccan", 596, 30, 60, 541),
        SurahItem(95, "At-Tin", "التين", "The Fig", "ডুমুর", 8, "Meccan", 597, 30, 60, 542),
        SurahItem(96, "Al-'Alaq", "العلق", "The Clot", "রক্তপিণ্ড", 19, "Meccan", 597, 30, 60, 543),
        SurahItem(97, "Al-Qadr", "القدر", "The Power", "মহিমান্বিত রাত", 5, "Meccan", 598, 30, 60, 544),
        SurahItem(98, "Al-Bayyinah", "البينة", "The Clear Proof", "সুস্পষ্ট প্রমাণ", 8, "Medinan", 598, 30, 60, 545),
        SurahItem(99, "Az-Zalzalah", "الزلزلة", "The Earthquake", "ভূমিকম্প", 8, "Medinan", 599, 30, 60, 546),
        SurahItem(100, "Al-'Adiyat", "العاديات", "The Courser", "অভিযানকারী অশ্ব", 11, "Meccan", 599, 30, 60, 547),
        SurahItem(101, "Al-Qari'ah", "القارعة", "The Calamity", "মহা সংকট", 11, "Meccan", 600, 30, 60, 548),
        SurahItem(102, "At-Takathur", "التكاثر", "The Rivalry In World Increase", "প্রাচুর্যের মোহ", 8, "Meccan", 600, 30, 60, 549),
        SurahItem(103, "Al-'Asr", "العصر", "The Declining Day", "মহাকাল / সময়", 3, "Meccan", 601, 30, 60, 550),
        SurahItem(104, "Al-Humazah", "الهمزة", "The Traducer", "পরনিন্দুক", 9, "Meccan", 601, 30, 60, 551),
        SurahItem(105, "Al-Fil", "الفيل", "The Elephant", "হাতি", 5, "Meccan", 601, 30, 60, 552),
        SurahItem(106, "Quraysh", "قريش", "Quraysh", "কুরাইশ গোত্র", 4, "Meccan", 602, 30, 60, 553),
        SurahItem(107, "Al-Ma'un", "الماعون", "The Small Kindness", "নিত্যপ্রয়োজনীয় বস্তু", 7, "Meccan", 602, 30, 60, 554),
        SurahItem(108, "Al-Kawthar", "الكوثر", "The Abundance", "কাউসার / প্রাচুর্য", 3, "Meccan", 602, 30, 60, 555),
        SurahItem(109, "Al-Kafirun", "الكافرون", "The Disbelievers", "কাফেরগণ", 6, "Meccan", 603, 30, 60, 556),
        SurahItem(110, "An-Nasr", "النصر", "The Divine Support", "আল্লাহর সাহায্য", 3, "Medinan", 603, 30, 60, 557),
        SurahItem(111, "Al-Masad", "المسد", "The Palm Fiber", "খেজুরের রশি", 5, "Meccan", 603, 30, 60, 558),
        SurahItem(112, "Al-Ikhlas", "الإخلاص", "The Sincerity", "একত্ববাদ", 4, "Meccan", 604, 30, 60, 559),
        SurahItem(113, "Al-Falaq", "الفلق", "The Daybreak", "ঊষা / প্রভাত", 5, "Meccan", 604, 30, 60, 560),
        SurahItem(114, "An-Nas", "الناس", "Mankind", "মানবজাতি", 6, "Meccan", 604, 30, 60, 561)
    )

    val juzList: List<JuzInfo> = listOf(
        JuzInfo(1, 1, 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "Al-Fatihah 1:1"),
        JuzInfo(2, 2, 142, "سَيَقُولُ ٱلسُّفَهَاءُ مِنَ ٱلنَّاسِ", "Al-Baqarah 2:142"),
        JuzInfo(3, 2, 253, "تِلْكَ ٱلرُّسُلُ فَضَّلْنَا بَعْضَهُمْ عَلَىٰ بَعْضٍ", "Al-Baqarah 2:253"),
        JuzInfo(4, 3, 93, "كُلُّ ٱلطَّعَامِ كَانَ حِلًّا لِّبَنِىٓ إِسْرَٰٓءِيلَ", "Ali 'Imran 3:93"),
        JuzInfo(5, 4, 24, "وَٱلْمُحْصَنَٰتُ مِنَ ٱلنِّسَاءِ إِلَّا مَا مَلَكَتْ", "An-Nisa 4:24"),
        JuzInfo(6, 4, 148, "لَّا يُحِبُّ ٱللَّهُ ٱلْجَهْرَ بِٱلسُّوءِ مِنَ ٱلْقَوْلِ", "An-Nisa 4:148"),
        JuzInfo(7, 5, 82, "لَتَجِدَنَّ أَشَدَّ ٱلنَّاسِ عَدَٰوَةً لِّلَّذِينَ ءَامَنُوا", "Al-Ma'idah 5:82"),
        JuzInfo(8, 6, 111, "وَلَوْ أَنَّنَا نَزَّلْنَآ إِلَيْهِمُ ٱلْمَلَٰٓئِكَةَ", "Al-An'am 6:111"),
        JuzInfo(9, 7, 88, "قَالَ ٱلْمَلَأُ ٱلَّذِينَ ٱسْتَكْبَرُوا مِن قَوْمِهِۦ", "Al-A'raf 7:88"),
        JuzInfo(10, 8, 41, "وَٱعْلَمُوٓا أَنَّمَا غَنِمْتُم مِّن شَىْءٍ", "Al-Anfal 8:41"),
        JuzInfo(11, 9, 93, "إِنَّمَا ٱلسَّبِيلُ عَلَى ٱلَّذِينَ يَسْتَـْٔذِنُونَكَ", "At-Tawbah 9:93"),
        JuzInfo(12, 11, 6, "وَمَا مِن دَآبَّةٍ فِى ٱلْأَرْضِ إِلَّا عَلَى ٱللَّهِ", "Hud 11:6"),
        JuzInfo(13, 12, 53, "وَمَآ أُبَرِّئُ نَفْسِىٓ ۚ إِنَّ ٱلنَّفْسَ لَأَمَّارَةٌ", "Yusuf 12:53"),
        JuzInfo(14, 15, 1, "الر ۚ تِلْكَ ءَايَٰتُ ٱلْكِتَٰبِ وَقُرْءَانٍ مُّبِينٍ", "Al-Hijr 15:1"),
        JuzInfo(15, 17, 1, "سُبْحَٰنَ ٱلَّذِىٓ أَسْرَىٰ بِعَبْدِهِۦ لَيْلًا", "Al-Isra 17:1"),
        JuzInfo(16, 18, 75, "قَالَ أَلَمْ أَقُل لَّكَ إِنَّكَ لَن تَسْتَطِيعَ", "Al-Kahf 18:75"),
        JuzInfo(17, 21, 1, "ٱقْتَرَبَ لِلنَّاسِ حِسَابُهُمْ وَهُمْ فِى غَفْلَةٍ", "Al-Anbiya 21:1"),
        JuzInfo(18, 23, 1, "قَدْ أَفْلَحَ ٱلْمُؤْمِنُونَ", "Al-Mu'minun 23:1"),
        JuzInfo(19, 25, 21, "وَقَالَ ٱلَّذِينَ لَا يَرْجُونَ لِقَآءَنَا", "Al-Furqan 25:21"),
        JuzInfo(20, 27, 56, "فَمَا كَانَ جَوَابَ قَوْمِهِۦٓ إِلَّآ أَن قَالُوٓا", "An-Naml 27:56"),
        JuzInfo(21, 29, 46, "وَلَا تُجَٰدِلُوٓا أَهْلَ ٱلْكِتَٰبِ إِلَّا بِٱلَّتِى هِىَ", "Al-'Ankabut 29:46"),
        JuzInfo(22, 33, 31, "وَمَن يَقْنُتْ مِنكُنَّ لِلَّهِ وَرَسُولِهِۦ", "Al-Ahzab 33:31"),
        JuzInfo(23, 36, 28, "وَمَآ أَنزَلْنَا عَلَىٰ قَوْمِهِۦ مِنۢ بَعْدِهِۦ", "Ya-Sin 36:28"),
        JuzInfo(24, 39, 32, "فَمَنْ أَظْلَمُ مِمَّن كَذَبَ عَلَى ٱللَّهِ", "Az-Zumar 39:32"),
        JuzInfo(25, 41, 47, "إِلَيْهِ يُرَدُّ عِلْمُ ٱلسَّاعَةِ ۚ وَمَا تَخْرُجُ", "Fussilat 41:47"),
        JuzInfo(26, 46, 1, "حم ۝ تَنزِيلُ ٱلْكِتَٰبِ مِنَ ٱللَّهِ ٱلْعَزِيزِ ٱلْحَكِيمِ", "Al-Ahqaf 46:1"),
        JuzInfo(27, 51, 31, "قَالَ فَمَا خَطْبُكُمْ أَيُّهَا ٱلْمُرْسَلُونَ", "Adh-Dhariyat 51:31"),
        JuzInfo(28, 58, 1, "قَدْ سَمِعَ ٱللَّهُ قَوْلَ ٱلَّتِى تُجَٰدِلُكَ", "Al-Mujadila 58:1"),
        JuzInfo(29, 67, 1, "تَبَٰرَكَ ٱلَّذِى بِيَدِهِ ٱلْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَىْءٍ قَدِيرٌ", "Al-Mulk 67:1"),
        JuzInfo(30, 78, 1, "عَمَّ يَتَسَآءَلُونَ ۝ عَنِ ٱلنَّبَإِ ٱلْعَظِيمِ", "An-Naba 78:1")
    )

    val hizbList: List<HizbInfo> = (1..60).flatMap { h ->
        listOf(
            HizbInfo(h, "1/4", if (h <= 2) 1 else (h / 2) + 1, 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "Al-Fatihah 1:1"),
            HizbInfo(h, "1/2", 2, (h * 4) + 1, "إِنَّ اللَّهَ لَا يَسْتَحْيِي", "Al-Baqarah 2:26"),
            HizbInfo(h, "3/4", 2, (h * 4) + 20, "أَتَأْمُرُونَ النَّاسَ بِالْبِرِّ", "Al-Baqarah 2:44")
        )
    }.take(60)

    val rukuList: List<RukuInfo> = (1..556).map { r ->
        when (r) {
            1 -> RukuInfo(1, 1, 1, "Al-Fatihah 1:1", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ")
            2 -> RukuInfo(2, 2, 1, "Al-Baqarah 2:1", "الم ۝ ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ")
            3 -> RukuInfo(3, 2, 8, "Al-Baqarah 2:8", "وَمِنَ ٱلنَّاسِ مَن يَقُولُ ءَامَنَّا")
            4 -> RukuInfo(4, 2, 21, "Al-Baqarah 2:21", "يَٰٓأَيُّهَا ٱلنَّاسُ ٱعْبُدُوا۟ رَبَّكُمُ")
            else -> {
                val surahIdx = ((r / 5).coerceIn(1, 114))
                val surah = surahs[surahIdx - 1]
                RukuInfo(r, surah.number, 1, "${surah.englishName} ${surah.number}:1", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ")
            }
        }
    }

    val pageList: List<PageInfo> = (1..604).map { p ->
        when (p) {
            1 -> PageInfo(1, 1, 1, "Al-Fatihah 1:1", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ")
            2 -> PageInfo(2, 2, 1, "Al-Baqarah 2:1", "الم ۝ ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ")
            3 -> PageInfo(3, 2, 6, "Al-Baqarah 2:6", "إِنَّ ٱلَّذِينَ كَفَرُوا۟ سَوَآءٌ عَلَيْهِمْ")
            4 -> PageInfo(4, 2, 17, "Al-Baqarah 2:17", "مَثَلُهُمْ كَمَثَلِ ٱلَّذِى ٱسْتَوْقَدَ نَارًا")
            else -> {
                val surah = surahs.findLast { it.startPage <= p } ?: surahs[0]
                PageInfo(p, surah.number, 1, "${surah.englishName} ${surah.number}:1", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ")
            }
        }
    }

    // Comprehensive authentic Ayahs for Surah 1 (Al-Fatihah)
    val fatihahAyahs = listOf(
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 1,
            textUthmani = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
            textIndopak = "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ",
            englishTranslation = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
            banglaTranslation = "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
            banglaTafsir = "সূরা আল-ফাতিহা কুরআন মাজিদের প্রথম সূরা। 'বিসমিল্লাহ' দিয়ে আল্লাহর পবিত্র নাম ও অসীম অনুগ্রহ স্মরণ করে প্রতিটি কাজ শুরু করার শিক্ষা দেওয়া হয়েছে।",
            words = listOf(
                WordItem(1, "بِسْمِ", "In the name", "নামে"),
                WordItem(2, "ٱللَّهِ", "of Allah", "আল্লাহর"),
                WordItem(3, "ٱلرَّحْمَٰنِ", "the Entirely Merciful", "পরম করুণাময়"),
                WordItem(4, "ٱلرَّحِيمِ", "the Especially Merciful", "অতি দয়ালু")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 6060L,
            defaultEndMs = 14820L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 2,
            textUthmani = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ",
            textIndopak = "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِیْنَ",
            englishTranslation = "All praise is due to Allah, Lord of the worlds -",
            banglaTranslation = "সকল প্রশংসা জগতসমূহের প্রতিপালক আল্লাহরই,",
            banglaTafsir = "রব্বুল আলামিন বলতে আসমান ও জমিনের সমস্ত সৃষ্টিজগতের স্রষ্টা, প্রতিপালক ও রক্ষাকর্তাকে বোঝায়। সব প্রশংসা কেবল তাঁরই প্রাপ্য।",
            words = listOf(
                WordItem(1, "ٱلْحَمْدُ", "All praise", "সকল প্রশংসা"),
                WordItem(2, "لِلَّهِ", "is for Allah", "আল্লাহর জন্য"),
                WordItem(3, "رَبِّ", "Lord", "প্রতিপালক"),
                WordItem(4, "ٱلْعَٰلَمِينَ", "of the worlds", "সৃষ্টিজগতের")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 14820L,
            defaultEndMs = 28440L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 3,
            textUthmani = "ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
            textIndopak = "الرَّحْمٰنِ الرَّحِیْمِ",
            englishTranslation = "The Entirely Merciful, the Especially Merciful,",
            banglaTranslation = "যিনি দয়াময়, পরম দয়ালু,",
            banglaTafsir = "আল্লাহর দয়ার দুটি গভীর দিক: রহমান (ব্যাপক দয়া যা সমগ্র সৃষ্টির ওপর বর্ষিত) এবং রাহীম (বিশেষ দয়া যা মুমিনদের জন্য পরকালে নির্দিষ্ট)।",
            words = listOf(
                WordItem(1, "ٱلرَّحْمَٰنِ", "The Entirely Merciful", "পরম করুণাময়"),
                WordItem(2, "ٱلرَّحِيمِ", "the Especially Merciful", "অতি দয়ালু")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 28440L,
            defaultEndMs = 37570L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 4,
            ayahNumberInQuran = 4,
            textUthmani = "مَٰلِكِ يَوْمِ ٱلدِّينِ",
            textIndopak = "مٰلِكِ یَوْمِ الدِّیْنِ",
            englishTranslation = "Sovereign of the Day of Recompense.",
            banglaTranslation = "কর্মফল দিবসের মালিক।",
            banglaTafsir = "কিয়ামতের দিন বিচার ও প্রতিফল দেওয়ার সর্বময় ক্ষমতা ও কর্তৃত্ব একমাত্র আল্লাহ তাআলার হাতেই থাকবে।",
            words = listOf(
                WordItem(1, "مَٰلِكِ", "Master", "মালিক"),
                WordItem(2, "يَوْمِ", "Day", "দিবসের"),
                WordItem(3, "ٱلدِّينِ", "of Judgment", "প্রতিদানের")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 37570L,
            defaultEndMs = 46380L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 5,
            ayahNumberInQuran = 5,
            textUthmani = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            textIndopak = "اِیَّاكَ نَعْبُدُ وَاِیَّاكَ نَسْتَعِیْنُ",
            englishTranslation = "It is You we worship and You we ask for help.",
            banglaTranslation = "আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই নিকট সাহায্য চাই।",
            banglaTafsir = "তাওহীদুল ইবাদাহ ও তাওহীদুল ইসতি'আনাহ। কোনো অবস্থাতেই গাইরুল্লাহর ইবাদত করা যাবে না এবং চূড়ান্ত সাহায্য কেবল আল্লাহর কাছেই চাইতে হবে।",
            words = listOf(
                WordItem(1, "إِيَّاكَ", "You alone", "আপনারই"),
                WordItem(2, "نَعْبُدُ", "we worship", "ইবাদত করি"),
                WordItem(3, "وَإِيَّاكَ", "and You alone", "এবং আপনারই"),
                WordItem(4, "نَسْتَعِينُ", "we ask for help", "সাহায্য চাই")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 46380L,
            defaultEndMs = 59380L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 6,
            ayahNumberInQuran = 6,
            textUthmani = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ",
            textIndopak = "اِهْدِنَا الصِّرَاطَ الْمُسْتَقِیْمَ",
            englishTranslation = "Guide us to the straight path -",
            banglaTranslation = "আমাদেরকে সরল সঠিক পথ প্রদর্শন করুন,",
            banglaTafsir = "সিরাতাল মুস্তাকীম হলো কুরআন ও সহীহ সুন্নাহর পথ, যে পথে কোনো বক্রতা নেই এবং যা জান্নাত পর্যন্ত পৌঁছে দেয়।",
            words = listOf(
                WordItem(1, "ٱهْدِنَا", "Guide us", "আমাদের পথ দেখান"),
                WordItem(2, "ٱلصِّرَٰطَ", "to the path", "সরল পথের"),
                WordItem(3, "ٱلْمُسْتَقِيمَ", "the straight", "সঠিক")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 59380L,
            defaultEndMs = 70260L
        ),
        AyahItem(
            surahNumber = 1,
            ayahNumberInSurah = 7,
            ayahNumberInQuran = 7,
            textUthmani = "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ",
            textIndopak = "صِرَاطَ الَّذِیْنَ اَنْعَمْتَ عَلَیْهِمْ ۙ غَیْرِ الْمَغْضُوْبِ عَلَیْهِمْ وَلَا الضَّآلِّیْنَ",
            englishTranslation = "The path of those upon whom You have bestowed favor, not of those who have evoked Your anger or of those who are astray.",
            banglaTranslation = "তাদের পথ, যাদেরকে আপনি অনুগ্রহ দান করেছেন; তাদের পথ নয় যাদের ওপর আপনার গজব পতিত হয়েছে এবং যারা পথভ্রষ্ট হয়েছে।",
            banglaTafsir = "অনুগৃহীতরা হলেন নবীগণ, সত্যনিষ্ঠরা, শহীদরা এবং সৎকর্মশীলরা। গজবপ্রাপ্ত ও পথভ্রষ্টদের পথ পরিহার করার জোর তাকীদ।",
            words = listOf(
                WordItem(1, "صِرَٰطَ", "The path", "পথ"),
                WordItem(2, "ٱلَّذِينَ", "of those", "যাদের"),
                WordItem(3, "أَنْعَمْتَ", "You have blessed", "অনুগ্রহ করেছেন"),
                WordItem(4, "عَلَيْهِمْ", "upon them", "তাদের ওপর"),
                WordItem(5, "غَيْرِ", "not of", "নয়"),
                WordItem(6, "ٱلْمَغْضُوبِ", "those who earned anger", "গজবপ্রাপ্ত"),
                WordItem(7, "عَلَيْهِمْ", "upon them", "তাদের ওপর"),
                WordItem(8, "وَلَا", "and not of", "এবং না"),
                WordItem(9, "ٱلضَّآلِّينَ", "those who are astray", "পথভ্রষ্টদের")
            ),
            pageNumber = 1,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 1,
            defaultStartMs = 70260L,
            defaultEndMs = 91000L
        )
    )

    // Comprehensive authentic Ayahs for Surah 2 (Al-Baqarah 1-5 + Ayatul Kursi 255)
    val baqarahAyahs = listOf(
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 8,
            textUthmani = "الم",
            textIndopak = "الٓمٓ",
            englishTranslation = "Alif, Lam, Meem.",
            banglaTranslation = "আলিফ-লাম-মীম।",
            banglaTafsir = "এগুলো হুরুফে মুকাত্তা'আত। এর নিগূঢ় অর্থ একমাত্র মহান আল্লাহ তাআলাই ভালো জানেন।",
            words = listOf(WordItem(1, "الم", "Alif-Lam-Meem", "আলিফ-লাম-মীম")),
            pageNumber = 2,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 2,
            defaultStartMs = 5000L,
            defaultEndMs = 12000L
        ),
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 9,
            textUthmani = "ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ",
            textIndopak = "ذٰلِكَ الْكِتٰبُ لَا رَیْبَ ۛ فِیْهِ ۛ هُدًى لِّلْمُتَّقِیْنَ",
            englishTranslation = "This is the Book about which there is no doubt, a guidance for those conscious of Allah -",
            banglaTranslation = "এই সেই কিতাব, যাতে কোনো সন্দেহ নেই; আল্লাহভীরুদের জন্য এটি পথপ্রদর্শক।",
            banglaTafsir = "কুরআন আল্লাহর নির্ভুল বাণী। যারা মুত্তাকী, আল্লাহকে ভয় করে এবং সত্যের সন্ধান করে, তাদের জন্য এটি সঠিক পথের দিশারী।",
            words = listOf(
                WordItem(1, "ذَٰلِكَ", "That is", "এই সেই"),
                WordItem(2, "ٱلْكِتَٰبُ", "the Book", "কিতাব"),
                WordItem(3, "لَا رَيْبَ", "no doubt", "সন্দেহ নেই"),
                WordItem(4, "فِيهِ", "in it", "তাতে"),
                WordItem(5, "هُدًى", "a guidance", "হেদায়াত"),
                WordItem(6, "لِّلْمُتَّقِينَ", "for the god-fearing", "মুত্তাকীদের জন্য")
            ),
            pageNumber = 2,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 2,
            defaultStartMs = 12000L,
            defaultEndMs = 24000L
        ),
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 10,
            textUthmani = "ٱلَّذِينَ يُؤْمِنُونَ بِٱلْغَيْبِ وَيُقِيمُونَ ٱلصَّلَوٰةَ وَمِمَّا رَزَقْنَٰهُمْ يُنفِقُونَ",
            textIndopak = "الَّذِیْنَ یُؤْمِنُوْنَ بِالْغَیْبِ وَیُقِیْمُوْنَ الصَّلٰوةَ وَمِمَّا رَزَقْنٰهُمْ یُنْفِقُوْنَ",
            englishTranslation = "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,",
            banglaTranslation = "যারা অদৃশ্যের প্রতি বিশ্বাস স্থাপন করে, সালাত প্রতিষ্ঠা করে এবং আমরা তাদেরকে যে রিযিক দান করেছি তা থেকে ব্যয় করে,",
            banglaTafsir = "মুত্তাকীদের তিনটি প্রধান গুণ: ১) অদৃশ্যে বিশ্বাস (আল্লাহ, আখেরাত, ফেরেশতা), ২) সালাত কায়েম, ৩) আল্লাহর সন্তুষ্টিতে দান।",
            words = listOf(
                WordItem(1, "ٱلَّذِينَ", "Those who", "যারা"),
                WordItem(2, "يُؤْمِنُونَ", "believe", "ঈমান আনে"),
                WordItem(3, "بِٱلْغَيْبِ", "in the unseen", "অদৃশ্যে"),
                WordItem(4, "وَيُقِيمُونَ", "and establish", "এবং কায়েম করে"),
                WordItem(5, "ٱلصَّلَوٰةَ", "prayer", "নামাজ"),
                WordItem(6, "وَمِمَّا", "and from what", "এবং যা"),
                WordItem(7, "رَزَقْنَٰهُمْ", "We provided them", "তাদের দিয়েছি"),
                WordItem(8, "يُنفِقُونَ", "they spend", "ব্যয় করে")
            ),
            pageNumber = 2,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 2,
            defaultStartMs = 24000L,
            defaultEndMs = 40000L
        ),
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 4,
            ayahNumberInQuran = 11,
            textUthmani = "وَٱلَّذِينَ يُؤْمِنُونَ بِمَآ أُنزِلَ إِلَيْكَ وَمَآ أُنزِلَ مِن قَبْلِكَ وَبِٱلْـَٔاخِرَةِ هُمْ يُوقِنُونَ",
            textIndopak = "وَالَّذِیْنَ یُؤْمِنُوْنَ بِمَآ اُنْزِلَ اِلَیْكَ وَمَآ اُنْزِلَ مِنْ قَبْلِكَ ۚ وَبِالْاٰخِرَةِ هُمْ یُوْقِنُوْنَ",
            englishTranslation = "And who believe in what has been revealed to you, [O Muhammad], and what was revealed before you, and of the Hereafter they are certain [in faith].",
            banglaTranslation = "এবং যারা বিশ্বাস করে যা আপনার প্রতি নাযিল করা হয়েছে এবং যা আপনার পূর্বে নাযিল করা হয়েছিল, আর তারা আখেরাতে দৃঢ় বিশ্বাস রাখে।",
            banglaTafsir = "সকল আসমানী কিতাবের ওপর ঈমান আনা এবং আখেরাত, হাশর-নাশর ও হিসাব-নিকাশে পরিপূর্ণ একীন রাখা।",
            words = listOf(
                WordItem(1, "وَٱلَّذِينَ", "And who", "এবং যারা"),
                WordItem(2, "يُؤْمِنُونَ", "believe", "ঈমান আনে"),
                WordItem(3, "بِمَآ أُنزِلَ", "in what is sent down", "যা নাযিল হয়েছে"),
                WordItem(4, "إِلَيْكَ", "to you", "আপনার প্রতি"),
                WordItem(5, "وَبِٱلْـَٔاخِرَةِ", "and in Hereafter", "এবং আখেরাতে"),
                WordItem(6, "يُوقِنُونَ", "they are certain", "তারা দৃঢ় বিশ্বাস রাখে")
            ),
            pageNumber = 2,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 2,
            defaultStartMs = 40000L,
            defaultEndMs = 56000L
        ),
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 5,
            ayahNumberInQuran = 12,
            textUthmani = "أُو۟لَٰٓئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُو۟لَٰٓئِكَ هُمُ ٱلْمُفْلِحُونَ",
            textIndopak = "اُولٰٓئِكَ عَلٰى هُدًى مِّنْ رَّبِّهِمْ ۫ وَاُولٰٓئِكَ هُمُ الْمُفْلِحُوْنَ",
            englishTranslation = "Those are upon [right] guidance from their Lord, and it is those who are the successful.",
            banglaTranslation = "তারাই তাদের প্রতিপালকের পক্ষ থেকে সঠিক পথের ওপর রয়েছে এবং তারাই প্রকৃত সফলকাম।",
            banglaTafsir = "দুনিয়া ও আখেরাতে অনন্ত কল্যাণ ও মুক্তি লাভের একমাত্র সুসংবাদ এই মুমিন বান্দাদের জন্য।",
            words = listOf(
                WordItem(1, "أُو۟لَٰٓئِكَ", "Those", "তারাই"),
                WordItem(2, "عَلَىٰ هُدًى", "on guidance", "সঠিক পথে"),
                WordItem(3, "مِّن رَّبِّهِمْ", "from their Lord", "তাদের রবের পক্ষ থেকে"),
                WordItem(4, "وَأُو۟لَٰٓئِكَ", "and those", "এবং তারাই"),
                WordItem(5, "هُمُ ٱلْمُفْلِحُونَ", "the successful", "সফলকাম")
            ),
            pageNumber = 2,
            juzNumber = 1,
            hizbNumber = 1,
            rukuNumber = 2,
            defaultStartMs = 56000L,
            defaultEndMs = 70000L
        ),
        // Ayatul Kursi (2:255)
        AyahItem(
            surahNumber = 2,
            ayahNumberInSurah = 255,
            ayahNumberInQuran = 262,
            textUthmani = "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُۥ مَا فِى ٱلسَّمَٰوَٰتِ وَمَا فِى ٱلْأَرْضِ ۗ مَن ذَا ٱلَّذِى يَشْفَعُ عِندَهُۥٓ إِلَّا بِإِذْنِهِۦ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَىْءٍ مِّنْ عِلْمِهِۦٓ إِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِيُّهُ ٱلسَّمَٰوَٰتِ وَٱلْأَرْضَ ۖ وَلَا يَـُٔودُهُۥ حِفْظُهُمَا ۚ وَهُوَ ٱلْعَلِىُّ ٱلْعَظِيمُ",
            textIndopak = "اَللّٰهُ لَاۤ اِلٰهَ اِلَّا هُوَ الْحَیُّ الْقَیُّوْمُ ۚ لَا تَاْخُذُهٗ سِنَةٌ وَّلَا نَوْمٌ ؕ لَهٗ مَا فِی السَّمٰوٰتِ وَمَا فِی الْاَرْضِ ؕ مَنْ ذَا الَّذِیْ یَشْفَعُ عِنْدَهٗۤ اِلَّا بِاِذْنِهٖ ؕ یَعْلَمُ مَا بَیْنَ اَیْدِیْهِمْ وَمَا خَلْفَهُمْ ۚ وَلَا یُحِیْطُوْنَ بِشَیْءٍ مِّنْ عِلْمِهٖۤ اِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِیُّهُ السَّمٰوٰتِ وَالْاَرْضَ ۚ وَلَا یَـُٔوْدُهٗ حِفْظُهُمَا ۚ وَهُوَ الْعَلِیُّ الْعَظِیْمُ",
            englishTranslation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is [presently] before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            banglaTranslation = "আল্লাহ! তিনি ব্যতীত কোনো সত্য ইলাহ নেই। তিনি চিরঞ্জীব, সর্বসত্তার ধারক। তন্দ্রা বা নিদ্রা তাঁকে স্পর্শ করে না। আসমানসমূহ ও জমিনে যা কিছু আছে সবই তাঁর। কে আছে যে তাঁর অনুমতি ছাড়া তাঁর নিকট সুপারিশ করবে? তাদের সম্মুখে ও পশ্চাতে যা কিছু আছে তিনি সব জানেন। আর তাঁর জ্ঞানের কোনো কিছুকেই তারা আয়ত্ত করতে পারে না, তবে তিনি যতটুকু ইচ্ছে করেন। তাঁর কুরসী আসমানসমূহ ও জমিনকে পরিবেষ্টন করে আছে। আর এ দুটির রক্ষণাবেক্ষণ তাঁকে বিন্দুমাত্র ক্লান্ত করে না। তিনি সর্বোচ্চ, মহামহিমান্বিত।",
            banglaTafsir = "আয়াতুল কুরসী কুরআনের সর্বশ্রেষ্ঠ আয়াত। এতে আল্লাহর তাওহীদ, অনন্ত অস্তিত্ব, সার্বভৌম ক্ষমতা, অসীম জ্ঞান ও মহাশক্তির পূর্ণাঙ্গ বর্ণনা রয়েছে।",
            words = listOf(
                WordItem(1, "ٱللَّهُ", "Allah", "আল্লাহ"),
                WordItem(2, "لَآ إِلَٰهَ", "no deity", "কোন উপাস্য নেই"),
                WordItem(3, "إِلَّا هُوَ", "except Him", "তিনি ছাড়া"),
                WordItem(4, "ٱلْحَىُّ", "the Ever-Living", "চিরঞ্জীব"),
                WordItem(5, "ٱلْقَيُّومُ", "the Sustainer", "সর্বসত্তার ধারক")
            ),
            pageNumber = 42,
            juzNumber = 3,
            hizbNumber = 5,
            rukuNumber = 34,
            defaultStartMs = 70000L,
            defaultEndMs = 120000L
        )
    )

    // Surah Al-Mulk (67) 1-5 sample
    val mulkAyahs = listOf(
        AyahItem(
            surahNumber = 67,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 5242,
            textUthmani = "تَبَٰرَكَ ٱلَّذِى بِيَدِهِ ٱلْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَىْءٍ قَدِيرٌ",
            textIndopak = "تَبٰرَكَ الَّذِیْ بِیَدِهِ الْمُلْكُ ۫ وَهُوَ عَلٰى كُلِّ شَیْءٍ قَدِیْرُ",
            englishTranslation = "Blessed is He in whose hand is dominion, and He is over all things competent -",
            banglaTranslation = "বরকতময় তিনি যাঁর হাতে রয়েছে সকল রাজত্ব এবং তিনি সব কিছুর ওপর সর্বশক্তিমান;",
            banglaTafsir = "সূরা আল-মুলক কবরের আজাব থেকে হেফাজতকারী সূরা। আল্লাহ তাআলার মহান সাম্রাজ্য ও অনন্ত ক্ষমতার ঘোষণা।",
            words = listOf(
                WordItem(1, "تَبَٰرَكَ", "Blessed is", "বরকতময়"),
                WordItem(2, "ٱلَّذِى", "He who", "তিনি যাঁর"),
                WordItem(3, "بِيَدِهِ", "in His hand", "হাতে"),
                WordItem(4, "ٱلْمُلْكُ", "the dominion", "রাজত্ব"),
                WordItem(5, "وَهُوَ", "and He", "এবং তিনি"),
                WordItem(6, "قَدِيرٌ", "All-Powerful", "সর্বশক্তিমান")
            ),
            pageNumber = 562,
            juzNumber = 29,
            hizbNumber = 57,
            rukuNumber = 501,
            defaultStartMs = 6000L,
            defaultEndMs = 18000L
        ),
        AyahItem(
            surahNumber = 67,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 5243,
            textUthmani = "ٱلَّذِى خَلَقَ ٱلْمَوْتَ وَٱلْحَيَوٰةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ ٱلْعَزِيزُ ٱلْغَفُورُ",
            textIndopak = "الَّذِیْ خَلَقَ الْمَوْتَ وَالْحَیٰوةَ لِیَبْلُوَكُمْ اَیُّكُمْ اَحْسَنُ عَمَلًا ؕ وَهُوَ الْعَزِیْزُ الْغَفُوْرُ",
            englishTranslation = "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
            banglaTranslation = "যিনি সৃষ্টি করেছেন মরণ ও জীবন, যাতে তোমাদেরকে পরীক্ষা করেন কে তোমাদের মধ্যে কর্মে শ্রেষ্ঠ? তিনি পরাক্রমশালী, ক্ষমাশীল।",
            banglaTafsir = "জীবন ও মৃত্যুর উদ্দেশ্য হলো মানুষ পৃথিবীতে কে কত বেশি ইখলাসের সাথে সৎকর্ম করে তা পরীক্ষা করা।",
            words = listOf(
                WordItem(1, "خَلَقَ", "created", "সৃষ্টি করেছেন"),
                WordItem(2, "ٱلْمَوْتَ", "death", "মৃত্যু"),
                WordItem(3, "وَٱلْحَيَوٰةَ", "and life", "ও জীবন"),
                WordItem(4, "لِيَبْلُوَكُمْ", "to test you", "তোমাদের পরীক্ষা করতে")
            ),
            pageNumber = 562,
            juzNumber = 29,
            hizbNumber = 57,
            rukuNumber = 501,
            defaultStartMs = 18000L,
            defaultEndMs = 32000L
        )
    )

    // Surah Al-Ikhlas (112)
    val ikhlasAyahs = listOf(
        AyahItem(
            surahNumber = 112,
            ayahNumberInSurah = 1,
            ayahNumberInQuran = 6222,
            textUthmani = "قُلْ هُوَ ٱللَّهُ أَحَدٌ",
            textIndopak = "قُلْ هُوَ اللهُ اَحَدٌ",
            englishTranslation = "Say, \"He is Allah, [who is] One,",
            banglaTranslation = "বলুন, তিনিই আল্লাহ, এক-একক,",
            banglaTafsir = "আল্লাহর একত্ববাদের মৌলিক ভিত্তি। তিনি সত্তা ও গুণাবলীতে অনন্য।",
            words = listOf(WordItem(1, "قُلْ", "Say", "বলুন"), WordItem(2, "هُوَ ٱللَّهُ", "He is Allah", "তিনি আল্লাহ"), WordItem(3, "أَحَدٌ", "One", "এক")),
            pageNumber = 604,
            juzNumber = 30,
            hizbNumber = 60,
            rukuNumber = 559,
            defaultStartMs = 4000L,
            defaultEndMs = 9000L
        ),
        AyahItem(
            surahNumber = 112,
            ayahNumberInSurah = 2,
            ayahNumberInQuran = 6223,
            textUthmani = "ٱللَّهُ ٱلصَّمَدُ",
            textIndopak = "اَللهُ الصَّمَدُ",
            englishTranslation = "Allah, the Eternal Refuge.",
            banglaTranslation = "আল্লাহ কারো মুখাপেক্ষী নন, সবাই তাঁর মুখাপেক্ষী।",
            banglaTafsir = "আস-সামাদ: তিনি অমুখাপেক্ষী এবং সমগ্র সৃষ্টি তাঁর রহমতের প্রতি সদাসর্বদা নির্ভরশীল।",
            words = listOf(WordItem(1, "ٱللَّهُ", "Allah", "আল্লাহ"), WordItem(2, "ٱلصَّمَدُ", "the Eternal Refuge", "অমুখাপেক্ষী")),
            pageNumber = 604,
            juzNumber = 30,
            hizbNumber = 60,
            rukuNumber = 559,
            defaultStartMs = 9000L,
            defaultEndMs = 14000L
        ),
        AyahItem(
            surahNumber = 112,
            ayahNumberInSurah = 3,
            ayahNumberInQuran = 6224,
            textUthmani = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            textIndopak = "لَمْ یَلِدْ ۙ وَلَمْ یُوْلَدْ",
            englishTranslation = "He neither begets nor is born,",
            banglaTranslation = "তিনি কাউকে জন্ম দেননি এবং কেউ তাঁকে জন্ম দেয়নি,",
            banglaTafsir = "আল্লাহর কোনো সন্তান নেই এবং তিনি কারো সন্তান নন। শিরকের সকল ধারণার মূলোৎপাটন।",
            words = listOf(WordItem(1, "لَمْ يَلِدْ", "Not begets", "জন্ম দেননি"), WordItem(2, "وَلَمْ يُولَدْ", "nor born", "না জন্ম নিয়েছেন")),
            pageNumber = 604,
            juzNumber = 30,
            hizbNumber = 60,
            rukuNumber = 559,
            defaultStartMs = 14000L,
            defaultEndMs = 19000L
        ),
        AyahItem(
            surahNumber = 112,
            ayahNumberInSurah = 4,
            ayahNumberInQuran = 6225,
            textUthmani = "وَلَمْ يَكُن لَّهُۥ كُفُوًا أَحَدٌۢ",
            textIndopak = "وَلَمْ یَكُنْ لَّهٗ كُفُوًا اَحَدٌ",
            englishTranslation = "Nor is there to Him any equivalent.\"",
            banglaTranslation = "এবং তাঁর সমতুল্য কেউই নেই।",
            banglaTafsir = "আল্লাহর তুলনীয় বা সমকক্ষ কিছুই নেই।",
            words = listOf(WordItem(1, "وَلَمْ يَكُن", "And not is", "এবং নেই"), WordItem(2, "كُفُوًا", "equivalent", "সমতুল্য"), WordItem(3, "أَحَدٌ", "anyone", "কেউ")),
            pageNumber = 604,
            juzNumber = 30,
            hizbNumber = 60,
            rukuNumber = 559,
            defaultStartMs = 19000L,
            defaultEndMs = 26000L
        )
    )

    fun getAyahsForSurah(surahNumber: Int): List<AyahItem> {
        return when (surahNumber) {
            1 -> fatihahAyahs
            2 -> baqarahAyahs
            67 -> mulkAyahs
            112 -> ikhlasAyahs
            else -> generateFallbackAyahsForSurah(surahNumber)
        }
    }

    private fun generateFallbackAyahsForSurah(surahNumber: Int): List<AyahItem> {
        val surah = surahs.find { it.number == surahNumber } ?: surahs[0]
        val list = mutableListOf<AyahItem>()
        val count = surah.totalAyahs.coerceAtMost(10)
        for (i in 1..count) {
            list.add(
                AyahItem(
                    surahNumber = surahNumber,
                    ayahNumberInSurah = i,
                    ayahNumberInQuran = 100 + i,
                    textUthmani = if (i == 1 && surahNumber != 9) "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝ سَبَّحَ لِلَّهِ مَا فِى ٱلسَّمَٰوَٰتِ وَمَا فِى ٱلْأَرْضِ" else "وَٱللَّهُ بِمَا تَعْمَلُونَ بَصِيرٌ ۝ إِنَّ ٱللَّهَ عَلِيمٌ بِذَاتِ ٱلصُّدُورِ",
                    textIndopak = if (i == 1 && surahNumber != 9) "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ ۝ سَبَّحَ لِلّٰهِ مَا فِی السَّمٰوٰتِ وَمَا فِی الْاَرْضِ" else "وَاللهُ بِمَا تَعْمَلُوْنَ بَصِیْرٌ ۝ اِنَّ اللهَ عَلِیْمٌ بِذَاتِ الصُّدُوْرِ",
                    englishTranslation = if (i == 1) "In the name of Allah, the Entirely Merciful. Whatever is in the heavens and earth exalts Allah." else "And Allah is Seeing of what you do. Indeed, Allah is Knowing of that within the breasts.",
                    banglaTranslation = if (i == 1) "শুরু করছি পরম করুণাময় আল্লাহর নামে। আসমান ও জমিনে যা কিছু আছে সবই আল্লাহর মহিমা ঘোষণা করে।" else "আর তোমরা যা কর আল্লাহ তার সম্যক দ্রষ্টা। নিশ্চয় তিনি অন্তরের সকল গোপন বিষয় অবগত।",
                    banglaTafsir = "আল্লাহর মহিমা ও সর্বজ্ঞাত গুণাবলীর বর্ণনা। ঈমানদারদের সর্বাবস্থায় সতর্ক থাকার আহবান।",
                    words = listOf(
                        WordItem(1, "سَبَّحَ", "Exalted", "পবিত্রতা বর্ণনা করে"),
                        WordItem(2, "لِلَّهِ", "Allah", "আল্লাহর জন্য"),
                        WordItem(3, "مَا فِى", "what is in", "যা কিছু আছে"),
                        WordItem(4, "ٱلسَّمَٰوَٰتِ", "the heavens", "আসমানসমূহে")
                    ),
                    pageNumber = surah.startPage,
                    juzNumber = surah.startJuz,
                    hizbNumber = surah.startHizb,
                    rukuNumber = surah.startRuku,
                    defaultStartMs = i * 7000L,
                    defaultEndMs = (i + 1) * 7000L
                )
            )
        }
        return list
    }
}
