import 'package:flutter/material.dart';

class ReciterSelectorScreen extends StatelessWidget {
  const ReciterSelectorScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Select Reciter'),
        centerTitle: true,
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(8),
        itemCount: 61,
        itemBuilder: (context, index) {
          final reciters = [
            'Abdulbasit Abdulsamad',
            'Abdur-Rahman as-Sudais',
            'Abu Bakr al-Shatri',
            'Ali Jaber',
            'Fares Abbad',
            'Hani ar-Rifai',
            'Khalid al-Jalil',
            'Maher Al Mueaqly',
            'Mishary Rashid Alafasy',
            'Mohamed Siddiq al-Minshawi',
            'Muhammad al-Luhaidan',
            'Musa Taha',
            'Nasser Al Qatami',
            'Sa\'ud as-Shuraim',
            'Saad al-Ghamdi',
            'Salah Al-Budair',
            'Samir Al-Manjirahi',
            'Thawaban Al Ajmi',
            'Uthman Al Thwaini',
            'Yousof Khanjar',
            'Ahmed Al Shukairi',
            'Jawed Basrawi',
            'Mustafa Ismail',
            'Ahmad Khader Al Tarawna',
            'Ahmad Al Agamy',
            'Amin Al Deen Mitwally',
            'Ali Al Hudhaifi',
            'Badr Al Budair',
            'Bashir Al Ajmi',
            'Ghamadi Al-Ohali',
            'Hamad Al Qanai',
            'Ibrahim Al Akhdar',
            'Ibrahim Al Dosari',
            'Jamal Al Qudsi',
            'Karim Manees Haggag',
            'Khalid Al Mousa',
            'Khalid Al Qahtani',
            'Koush Nuri',
            'Mahmoud Al Boghdadi',
            'Mahmoud Al Brekan',
            'Mahmoud Khalil Al-Husary',
            'Mohammad Al Tablawi',
            'Mohamed Abdo Dabab',
            'Mohamed Ahmed El Kurdi',
            'Mohamed Ayoub',
            'Mohamed Refaat',
            'Mohammed Al-Kurdi',
            'Mouhammad Toufiq',
            'Muhammad ash-Shuraim',
            'Muhammad Jibreel',
            'Muhammad Siddiq Al Minshawi',
            'Muhammad Youssuf Khanjar',
            'Nawal Al Kuwaiti',
            'Noor Al Deen Al Assass',
            'Qaari Abdulwali Ahmad Ayoub',
            'Saad Al Qureshi',
            'Sallah Bukhatir',
            'Saudi Al Shuraim',
            'Saud As Shuraim',
            'Tawfeeq Al Sawaigh',
            'Waleed Al Qahtani',
          ];

          return Card(
            margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            child: ListTile(
              leading: CircleAvatar(
                child: Text('${index + 1}'),
              ),
              title: Text(reciters[index % reciters.length]),
              subtitle: const Text('Renowned Quranic Reciter'),
              trailing: const Icon(Icons.play_circle_outline),
              onTap: () {
                // Select reciter and play
              },
            ),
          );
        },
      ),
    );
  }
}
