package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.AdminAdapter
import com.example.wallet.data.Admin
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseAdminActivity : AppCompatActivity() {

    private lateinit var rvAdmins: RecyclerView

    private lateinit var adminAdapter: AdminAdapter

    private val handler = Handler(Looper.getMainLooper())


    // Names that will only be displayed on screen
    private val randomNames = listOf(

        "Rahul Sharma",
        "Amit Kumar",
        "Rohan Singh",
        "Arjun Patel",
        "Karan Verma",
        "Vijay Kumar",
        "Ravi Sharma",
        "Ajay Singh",
        "Mohit Kumar",
        "Priya Sharma",
        "Ankit Verma",
        "Rohit Singh",
        "Deepak Kumar",
        "Manish Patel",
        "Akash Sharma",
        "Sumit Kumar",
        "Raj Verma",
        "Suresh Kumar",
        "Nikhil Sharma",
        "Vikas Singh",
        "Aarav Sharma",
        "Olivia Bennett",
        "Liam Carter",
        "Emma Wilson",
        "Noah Anderson",
        "Sophia Martinez",
        "Ethan Brooks",
        "Mia Thompson",
        "Lucas Johnson",
        "Ava Williams",
        "Alexander Brown",
        "Isabella Garcia",
        "Daniel Miller",
        "Amelia Davis",
        "Henry Wilson",
        "Charlotte Moore",
        "James Taylor",
        "Emily Clark",
        "Benjamin Lewis",
        "Grace Walker",
        "William Hall",
        "Lily Young",
        "Samuel Allen",
        "Ella King",
        "Jack Wright",
        "Chloe Scott",
        "Thomas Green",
        "Hannah Baker",
        "Oscar Adams",
        "Sophie Nelson",
        "Arthur Mitchell",
        "Jessica Roberts",
        "George Turner",
        "Ruby Phillips",
        "Charlie Campbell",
        "Evie Parker",
        "Harry Evans",
        "Isla Edwards",
        "Leo Collins",
        "Poppy Stewart",
        "Arjun Mehta",
        "Aanya Patel",
        "Rohan Verma",
        "Ananya Singh",
        "Vikram Rao",
        "Priya Kapoor",
        "Aditya Kumar",
        "Kavya Nair",
        "Rahul Malhotra",
        "Diya Shah",
        "Kenji Tanaka",
        "Yuki Nakamura",
        "Haruto Sato",
        "Aiko Yamamoto",
        "Ren Suzuki",
        "Hana Watanabe",
        "Daiki Ito",
        "Mei Kobayashi",
        "Sota Kato",
        "Sakura Mori",
        "Min-jun Kim",
        "Seo-yeon Park",
        "Ji-hoon Lee",
        "Ha-eun Choi",
        "Hyun-woo Jung",
        "Soo-jin Kang",
        "Tae-hyun Han",
        "Min-seo Yoon",
        "Joon-ho Shin",
        "Ye-jin Kwon",
        "Wei Zhang",
        "Li Wang",
        "Jun Chen",
        "Mei Liu",
        "Hao Yang",
        "Xinyi Huang",
        "Wei Lin",
        "Jia Zhao",
        "Ming Wu",
        "Lili Xu",
        "Muhammad Hassan",
        "Aisha Rahman",
        "Omar Abdullah",
        "Fatima Ali",
        "Ahmed Mahmoud",
        "Layla Ibrahim",
        "Yusuf Khan",
        "Mariam Farouk",
        "Hamza Khalid",
        "Noor Hussain",
        "Ivan Petrov",
        "Anna Ivanova",
        "Dmitri Volkov",
        "Elena Sokolova",
        "Alexei Morozov",
        "Natalia Kuznetsova",
        "Sergei Orlov",
        "Irina Popova",
        "Mikhail Kozlov",
        "Olga Romanova",
        "Matteo Rossi",
        "Giulia Romano",
        "Luca Ferrari",
        "Sofia Conti",
        "Marco Bianchi",
        "Chiara Moretti",
        "Alessandro Ricci",
        "Francesca Lombardi",
        "Davide Esposito",
        "Elena Marino",
        "Jean Dupont",
        "Camille Martin",
        "Louis Bernard",
        "Claire Laurent",
        "Antoine Moreau",
        "Juliette Simon",
        "Pierre Lefevre",
        "Amelie Girard",
        "Nicolas Fontaine",
        "Elise Mercier",
        "Hans Mueller",
        "Anna Schneider",
        "Lukas Fischer",
        "Lena Weber",
        "Felix Wagner",
        "Clara Becker",
        "Max Hoffmann",
        "Emma Richter",
        "Paul Keller",
        "Sophie Kraus",
        "Carlos Hernandez",
        "Maria Rodriguez",
        "Diego Fernandez",
        "Lucia Gonzalez",
        "Javier Torres",
        "Carmen Navarro",
        "Alejandro Ruiz",
        "Elena Moreno",
        "Miguel Castro",
        "Sofia Ortega",
        "Pedro Silva",
        "Ana Santos",
        "Rafael Oliveira",
        "Beatriz Costa",
        "Lucas Pereira",
        "Mariana Alves",
        "Gabriel Carvalho",
        "Camila Ribeiro",
        "Thiago Martins",
        "Julia Ferreira",
        "Juan Perez",
        "Valentina Morales",
        "Andres Ramirez",
        "Isabella Cruz",
        "Mateo Herrera",
        "Daniela Vargas",
        "Santiago Reyes",
        "Gabriela Mendoza",
        "Nicolas Castillo",
        "Catalina Rojas",
        "Ethan Williams",
        "Madison Harris",
        "Mason Martin",
        "Abigail Thompson",
        "Logan Davis",
        "Elizabeth Wilson",
        "Jacob Moore",
        "Scarlett Taylor",
        "Michael White",
        "Victoria Harris",
        "Christopher Clark",
        "Natalie Lewis",
        "Andrew Robinson",
        "Zoe Walker",
        "Matthew Young",
        "Lucy Hall",
        "David Allen",
        "Sarah King",
        "Joseph Wright",
        "Alice Green",
        "Daniel Cooper",
        "Megan Bailey",
        "Ryan Collins",
        "Lauren Richardson",
        "Nathan Ward",
        "Sophie Morris",
        "Dylan Cox",
        "Olivia Richardson",
        "Adam Gray",
        "Jessica James",
        "Noah Thompson",
        "Amelia Scott",
        "Ethan Robinson",
        "Grace Mitchell",
        "Mason Turner",
        "Chloe Parker",
        "Jacob Evans",
        "Ella Collins",
        "Benjamin Stewart",
        "Hannah Morgan",
        "Kwame Mensah",
        "Ama Asante",
        "Kofi Boateng",
        "Abena Owusu",
        "Kojo Adjei",
        "Akosua Frimpong",
        "Chinedu Okafor",
        "Adaeze Nwosu",
        "Emeka Obi",
        "Ngozi Eze",
        "Tunde Adeyemi",
        "Funmi Balogun",
        "Babatunde Adebayo",
        "Yetunde Ogunleye",
        "Samuel Okoro",
        "Grace Eze",
        "Daniel Mwangi",
        "Wanjiku Kamau",
        "Brian Otieno",
        "Akinyi Odhiambo",
        "Sipho Dlamini",
        "Nomsa Ndlovu",
        "Thabo Mokoena",
        "Lerato Molefe",
        "Themba Khumalo",
        "Naledi Maseko",
        "Jean-Pierre Mbala",
        "Chantal Kabila",
        "Alain Tshisekedi",
        "Mireille Kabongo",
        "Ahmed Benali",
        "Leila Haddad",
        "Karim Mansouri",
        "Samira Amrani",
        "Youssef Bensalem",
        "Nadia Cherif",
        "Tariq Mansour",
        "Huda Saleh",
        "Rami Nasser",
        "Dalia Fares",
        "Abdullah Al-Sayed",
        "Salma Al-Hassan",
        "Khalid Al-Rashid",
        "Rana Al-Masri",
        "Zain Abbas",
        "Yasmin Qureshi",
        "Bilal Siddiqui",
        "Sana Mirza",
        "Imran Farooqi",
        "Hiba Hashmi",
        "João Costa",
        "Mariana Souza",
        "Felipe Almeida",
        "Beatriz Oliveira",
        "Rafael Lima",
        "Larissa Rocha",
        "Bruno Santos",
        "Isabela Mendes",
        "Eduardo Barros",
        "Fernanda Cardoso",
        "Diego Silva",
        "Gabriela Lima",
        "Thiago Souza",
        "Renata Carvalho",
        "Marcelo Gomes",
        "Juliana Martins",
        "Caio Ribeiro",
        "Amanda Ferreira",
        "Vinicius Pereira",
        "Leticia Alves",
        "Alejandro Gomez",
        "Sofia Diaz",
        "Fernando Lopez",
        "Carla Romero",
        "Enrique Martinez",
        "Paula Sanchez",
        "Sergio Molina",
        "Laura Vidal",
        "Alberto Navarro",
        "Natalia Iglesias",
        "Hiroshi Tanaka",
        "Emi Suzuki",
        "Takumi Yamada",
        "Rin Nakamura",
        "Kaito Saito",
        "Yuna Kobayashi",
        "Riku Watanabe",
        "Ayaka Ito",
        "Shota Yamamoto",
        "Nao Fujimoto",
        "Daisuke Mori",
        "Miku Kondo",
        "Takeru Nakajima",
        "Reina Ogawa",
        "Kohei Inoue",
        "Misaki Arai",
        "Ryota Kimura",
        "Haruka Matsuda",
        "Kota Nishimura",
        "Nanami Hayashi",
        "Carlos Mendes",
        "Ana Pereira",
        "Victor Almeida",
        "Mariana Costa",
        "Bruno Carvalho",
        "Clara Santos",
        "Diego Moreira",
        "Laura Ribeiro",
        "Andre Fernandes",
        "Beatriz Correia",
        "Nicolas Laurent",
        "Emma Dubois",
        "Julien Moreau",
        "Louise Girard",
        "Gabriel Laurent",
        "Manon Lefevre",
        "Thomas Rousseau",
        "Pauline Bernard",
        "Hugo Mercier",
        "Charlotte Fontaine",
        "Erik Johansson",
        "Anna Lindberg",
        "Johan Andersson",
        "Elsa Nilsson",
        "Lars Bergstrom",
        "Ingrid Karlsson",
        "Oskar Lund",
        "Freja Eriksson",
        "Nils Holm",
        "Astrid Larsson",
        "Jan Novak",
        "Anna Kowalska",
        "Marek Nowak",
        "Zofia Zielinska",
        "Piotr Wozniak",
        "Katarzyna Mazur",
        "Tomasz Krawczyk",
        "Alicja Pawlak",
        "Jakub Krupa",
        "Magdalena Sikora",
        "Andrei Popescu",
        "Elena Ionescu",
        "Mihai Stan",
        "Ioana Dumitru",
        "Stefan Marin",
        "Ana Radu",
        "Victor Gheorghe",
        "Irina Matei",
        "Adrian Pavel",
        "Cristina Stoica",
        "George Papadopoulos",
        "Eleni Nikolaou",
        "Nikos Georgiou",
        "Maria Antoniou",
        "Andreas Christou",
        "Sofia Dimitriou",
        "Yannis Vassiliou",
        "Katerina Pappas",
        "Dimitris Angelou",
        "Anna Stavrou",
        "Mehmet Kaya",
        "Elif Demir",
        "Emre Yilmaz",
        "Zeynep Aydin",
        "Burak Ozdemir",
        "Selin Arslan",
        "Kerem Sahin",
        "Derya Kaplan",
        "Can Erdem",
        "Asli Kurt",
        "Maya Wilson",
        "Caleb Anderson",
        "Chloe Brown",
        "Nathan Miller",
        "Lily Davis",
        "Ryan Garcia",
        "Emma Martinez",
        "Aaron Rodriguez",
        "Sophia Hernandez",
        "Tyler Smith",
        "Natalie Johnson",
        "Brandon Lee",
        "Hannah Kim",
        "Jason Wong",
        "Jessica Chen",
        "Kevin Liu",
        "Rachel Zhang",
        "Justin Wang",
        "Nicole Huang",
        "Daniel Kim",
        "Sarah Park",
        "Andrew Lee",
        "Michelle Choi",
        "Brian Chen",
        "Amy Zhang",
        "Steven Wu",
        "Grace Liu",
        "Eric Wang",
        "Jennifer Lin",
        "Ahmed Ibrahim",
        "Sarah Hassan",
        "Mohamed Said",
        "Mariam Ahmed",
        "Hassan Osman",
        "Fatima Yusuf",
        "Ali Rahman",
        "Noor Ahmed",
        "Sami Khalil",
        "Lina Mahmoud",
        "Samuel Brown",
        "Rebecca Smith",
        "Joshua Wilson",
        "Megan Taylor",
        "Daniel Evans",
        "Laura Morgan",
        "Thomas Harris",
        "Rachel Cooper",
        "James Bennett",
        "Sophie Turner",
        "Oliver Parker",
        "Amelia Collins",
        "Jack Morgan",
        "Emily Richardson",
        "George Bailey",
        "Lucy Ward",
        "Harry Cooper",
        "Ella Morris",
        "Charlie Richardson",
        "Evie Cox",
        "Arjun Rao",
        "Meera Sharma",
        "Kabir Khan",
        "Nisha Verma",
        "Dev Patel",
        "Riya Kapoor",
        "Ishaan Malhotra",
        "Anika Desai",
        "Vihaan Joshi",
        "Tara Mehta",
        "Yash Agarwal",
        "Sneha Nair",
        "Aditya Reddy",
        "Pooja Iyer",
        "Karan Bhatia",
        "Simran Kaur",
        "Rohit Sinha",
        "Neha Gupta",
        "Aryan Choudhary",
        "Anjali Saxena",
        "William Anderson",
        "Sophia Bennett",
        "James Carter",
        "Isabella Wilson",
        "Alexander Taylor",
        "Charlotte Johnson",
        "Henry Brown",
        "Amelia Clark",
        "Edward Lewis",
        "Victoria Walker",
        "Mateo Garcia",
        "Lucia Fernandez",
        "Javier Rodriguez",
        "Sofia Martinez",
        "Diego Gonzalez",
        "Elena Lopez",
        "Marco Romano",
        "Giulia Ferrari",
        "Luca Bianchi",
        "Francesca Rossi",
        "Kenji Suzuki",
        "Yuki Tanaka",
        "Haruto Watanabe",
        "Aiko Nakamura",
        "Min-jun Kim",
        "Seo-yeon Park",
        "Ji-hoon Lee",
        "Ha-neul Choi",
        "Wei Zhang",
        "Mei Chen",
        "Jun Wang",
        "Li Huang",
        "Ahmed Khalid",
        "Aisha Mahmoud",
        "Omar Hassan",
        "Layla Rahman",
        "Ivan Petrov",
        "Elena Volkov",
        "Dmitri Sokolov",
        "Anna Morozova",
        "Kwame Asante",
        "Ama Mensah",
        "Daniel Okafor",
        "Ada Nwosu",
        "João Silva",
        "Maria Santos",
        "Felipe Costa",
        "Ana Oliveira",
        "Rafael Souza",
        "Camila Pereira",

    )


    // This runs every 20 seconds
    private val changeNamesRunnable = object : Runnable {

        override fun run() {

            // Create a random display name
            // for every admin card
            val newNames = List(
                adminAdapter.itemCount
            ) {

                randomNames.random()

            }

            // Update only displayed names
            adminAdapter.updateNames(
                newNames
            )


            // Run again after 20 seconds
            handler.postDelayed(
                this,
                20000
            )
        }
    }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_choose_admin
        )


        rvAdmins =
            findViewById(
                R.id.rvAdmins
            )


        rvAdmins.layoutManager =
            LinearLayoutManager(
                this
            )


        val type =
            intent.getStringExtra(
                "TYPE"
            )


        val userId =
            intent.getIntExtra(
                "USER_ID",
                0
            )


        // Get real admins from API
        RetrofitClient.api
            .getAllAdmins()

            .enqueue(
                object : Callback<List<Admin>> {


                    override fun onResponse(

                        call: Call<List<Admin>>,

                        response: Response<List<Admin>>

                    ) {


                        if (
                            response.isSuccessful
                        ) {


                            val admins =
                                response.body()
                                    ?: emptyList()


                            // Create initial random names
                            val firstNames =
                                List(
                                    admins.size
                                ) {

                                    randomNames.random()

                                }


                            // Create adapter
                            adminAdapter =
                                AdminAdapter(

                                    admins,

                                    firstNames.toMutableList()

                                ) { admin ->


                                    when (
                                        type
                                    ) {


                                        "BUY" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    AddFundUserActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }


                                        "SELL" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    WithdrawFundActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }


                                        "CHAT" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    HelpSupportActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }

                                    }

                                }


                            // Set adapter
                            rvAdmins.adapter =
                                adminAdapter


                            // Start changing names
                            // after 20 seconds
                            handler.postDelayed(

                                changeNamesRunnable,

                                20000

                            )

                        }

                    }


                    override fun onFailure(

                        call: Call<List<Admin>>,

                        t: Throwable

                    ) {

                        // API failed

                    }

                }

            )

    }


    override fun onDestroy() {

        super.onDestroy()


        // Stop changing names when
        // Activity is destroyed
        handler.removeCallbacks(
            changeNamesRunnable
        )

    }

}