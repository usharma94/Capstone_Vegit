
package sheridan.sharmupm.vegit_capstone.ui.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.ui.diet.DietViewModel
import sheridan.sharmupm.vegit_capstone.ui.home.HomeViewModel


//class DietFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = DietFragment()
//    }
//
//    private lateinit var viewModel: DietViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.diet_cardview, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(DietViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
//
//}

class DietFragment : Fragment() {

    private lateinit var dietViewModel: DietViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dietViewModel =
                ViewModelProvider(this).get(DietViewModel::class.java)
        val root = inflater.inflate(R.layout.diet_cardview, container, false)
        val textView: TextView = root.findViewById(R.id.text_diet)
        val dietType: TextView = root.findViewById(R.id.vegetarianDiet)
        dietViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}