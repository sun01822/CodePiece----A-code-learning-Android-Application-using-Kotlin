package com.example.codepiece.fragments

import DetailsScreenFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.R
import com.example.codepiece.adapter.SubProblemsAdapter
import com.example.codepiece.data.SubProblem
import com.example.codepiece.databinding.FragmentSubProblemsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class SubProblemsFragment : Fragment(), SubProblemsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentSubProblemsBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var problemName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubProblemsBinding.inflate(layoutInflater)
        problemName = arguments?.getString("problemName").toString()
        binding.textView.text = problemName

        // Initialize RecyclerView and set up the layout manager
        val recyclerView: RecyclerView = binding.subProblemsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //
        /*uploadProblemsToFirestore()*/

        // Fetch subProblems data from Firebase Firestore
        fetchSubProblemsData()
        return binding.root
    }

    private fun fetchSubProblemsData() {
        firestore.collection(problemName) // Replace with your collection name
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val subProblems = mutableListOf<SubProblem>()
                for (document in querySnapshot.documents) {
                    val problemNumber = document.getString("problemNumber") ?: ""
                    val problemName = document.getString("problemName") ?: ""
                    val problemAlgorithms = document.getString("problemAlgorithms") ?: ""
                    val cprogramCode = document.getString("cprogramCode") ?: ""
                    val cppCode = document.getString("cppCode") ?: ""
                    val javaCode = document.getString("javaCode") ?: ""
                    val pythonCode = document.getString("pythonCode") ?: ""
                    subProblems.add(SubProblem(problemNumber, problemName, problemAlgorithms, cprogramCode, cppCode, javaCode, pythonCode))
                }

                // Sort the subProblems list based on problemNumber
                subProblems.sortBy { it.problemNumber.toInt() }

                // Update the adapter with the sorted data
                val recyclerView: RecyclerView = binding.subProblemsRecyclerView
                val subProblemsAdapter = SubProblemsAdapter(subProblems, this)
                recyclerView.adapter = subProblemsAdapter
            }
            .addOnFailureListener {
                // Handle error here
                Toast.makeText(requireContext(), "Check your data connection", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    /* private fun uploadProblemsToFirestore() {
        val problems = listOf(
            SubProblem(
                "1",
                "Write a program to find maximum between two numbers.",
                "1.\tInput two numbers from user. Store it in some variable say num1 and num2.\n" +
                        "2.\tCheck if(num1 > num2) then print num1 is maximum.\n" +
                        "3.\tCheck if(num2 > num1) then print num2 is maximum.\n" +
                        "4.\tCheck if(num1 == num2) then both the numbers are equal.\n",
                "#include <stdio.h>\n" +
                        "\n" +
                        "int main() {\n" +
                        "    // Declare variables to store the input numbers\n" +
                        "    int num1, num2;\n" +
                        "\n" +
                        "    // Input two numbers from the user\n" +
                        "    printf(\"Enter the first number: \");\n" +
                        "    scanf(\"%d\", &num1);\n" +
                        "\n" +
                        "    printf(\"Enter the second number: \");\n" +
                        "    scanf(\"%d\", &num2);\n" +
                        "\n" +
                        "    // Check which number is larger\n" +
                        "    if (num1 > num2) {\n" +
                        "        printf(\"%d is the larger number.\\n\", num1);\n" +
                        "    } else if (num2 > num1) {\n" +
                        "        printf(\"%d is the larger number.\\n\", num2);\n" +
                        "    } else {\n" +
                        "        printf(\"Both numbers are equal.\\n\");\n" +
                        "    }\n" +
                        "\n" +
                        "    return 0;\n" +
                        "}\n",
                "#include <iostream>\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main() {\n" +
                        "    int num1, num2;\n" +
                        "\n" +
                        "    // Input two numbers\n" +
                        "    cout << \"Enter two numbers: \";\n" +
                        "    cin >> num1 >> num2;\n" +
                        "\n" +
                        "    if (num1 > num2) {\n" +
                        "        cout << \"The larger number is: \" << num1 << endl;\n" +
                        "    } else if (num2 > num1) {\n" +
                        "        cout << \"The larger number is: \" << num2 << endl;\n" +
                        "    } else {\n" +
                        "        cout << \"Both numbers are equal.\" << endl;\n" +
                        "    }\n" +
                        "\n" +
                        "    return 0;\n" +
                        "}\n",
                "import java.util.Scanner;\n" +
                        "\n" +
                        "public class FindLargerNumber {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Scanner scanner = new Scanner(System.in);\n" +
                        "\n" +
                        "        System.out.print(\"Enter the first number: \");\n" +
                        "        int num1 = scanner.nextInt();\n" +
                        "\n" +
                        "        System.out.print(\"Enter the second number: \");\n" +
                        "        int num2 = scanner.nextInt();\n" +
                        "\n" +
                        "        if (num1 > num2) {\n" +
                        "            System.out.println(num1 + \" is the larger number.\");\n" +
                        "        } else if (num2 > num1) {\n" +
                        "            System.out.println(num2 + \" is the larger number.\");\n" +
                        "        } else {\n" +
                        "            System.out.println(\"Both numbers are equal.\");\n" +
                        "        }\n" +
                        "\n" +
                        "        scanner.close();\n" +
                        "    }\n" +
                        "}\n",
                "# Input two numbers from the user\n" +
                        "num1 = float(input(\"Enter the first number: \"))\n" +
                        "num2 = float(input(\"Enter the second number: \"))\n" +
                        "\n" +
                        "# Compare the numbers and find the larger one\n" +
                        "if num1 > num2:\n" +
                        "    print(num1, \"is the larger number.\")\n" +
                        "elif num2 > num1:\n" +
                        "    print(num2, \"is the larger number.\")\n" +
                        "else:\n" +
                        "    print(\"Both numbers are equal.\")\n"
            ),
            SubProblem(
                "2",
                "Write a program to find maximum between three numbers",
                "",
                "",
                "",
                "",
                ""
            )
        )

        for (problem in problems) {
            firestore.collection(problemName) // Replace with your collection name
                .add(problem)
                .addOnSuccessListener { documentReference ->
                    // Problem uploaded successfully
                    Toast.makeText(requireContext(), "Problem uploaded", Toast.LENGTH_SHORT).show()
                    println("Problem uploaded: ${documentReference.id}")
                }
                .addOnFailureListener { exception ->
                    // Handle error here
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    println("Error uploading problem: $exception")
                }
        }
    }*/

    override fun onItemClick(subProblem: SubProblem) {
        val bundle = Bundle().apply {
            putString("problemNumber", subProblem.problemNumber)
            putString("problemName", subProblem.problemName)
            putString("problemAlgorithms", subProblem.problemAlgorithms)
            putString("cprogramCode", subProblem.cprogramCode)
            putString("cppCode", subProblem.cppCode)
            putString("javaCode", subProblem.javaCode)
            putString("pythonCode", subProblem.pythonCode)
        }

        val detailsFragment = DetailsScreenFragment()
        detailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
