import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codepiece.R
import com.example.codepiece.databinding.FragmentDetailsScreenBinding

class DetailsScreenFragment : Fragment() {
    private lateinit var binding: FragmentDetailsScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from the bundle
        val problemNumber = arguments?.getString("problemNumber")
        val problemName = arguments?.getString("problemName")
        val problemAlgorithms = arguments?.getString("problemAlgorithms")
        val cprogramCode = arguments?.getString("cprogramCode")
        val cppCode = arguments?.getString("cppCode")
        val javaCode = arguments?.getString("javaCode")
        val pythonCode = arguments?.getString("pythonCode")

        // Set data to the layout's views using View Binding
        binding.problemNumberTextView.text = problemNumber
        binding.problemNameTextView.text = problemName
        binding.problemAlgorithmsTextView.text = problemAlgorithms

        // Set the default selected state for the cButton and codeTextView
        setButtonStyle(binding.cButton)
        resetButtonStyles(binding.cppButton, binding.javaButton, binding.pythonButton)
        binding.codeTextView.text = cprogramCode

        // Set click listeners for buttons
        binding.cppButton.setOnClickListener {
            // Update the text of codeTextView to show C++ code
            binding.codeTextView.text = cppCode

            // Change the button style of cppButton
            setButtonStyle(binding.cppButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.javaButton, binding.pythonButton
            )
        }

        binding.cButton.setOnClickListener {
            // Update the text of codeTextView to show C code
            binding.codeTextView.text = cprogramCode

            // Change the button style of cButton
            setButtonStyle(binding.cButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cppButton, binding.javaButton, binding.pythonButton
            )
        }

        binding.javaButton.setOnClickListener {
            // Update the text of codeTextView to show Java code
            binding.codeTextView.text = javaCode

            // Change the button style of javaButton
            setButtonStyle(binding.javaButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.cppButton, binding.pythonButton
            )
        }

        binding.pythonButton.setOnClickListener {
            // Update the text of codeTextView to show Python code
            binding.codeTextView.text = pythonCode

            // Change the button style of pythonButton
            setButtonStyle(binding.pythonButton)

            // Reset button style of other buttons
            resetButtonStyles(
                binding.cButton, binding.cppButton, binding.javaButton
            )
        }
    }

    private fun setButtonStyle(button: Button) {
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorAccent))
    }

    private fun resetButtonStyles(vararg buttons: Button) {
        for (button in buttons) {
            button.setTextColor(ContextCompat.getColor(button.context, android.R.color.black))
        }
    }
}
