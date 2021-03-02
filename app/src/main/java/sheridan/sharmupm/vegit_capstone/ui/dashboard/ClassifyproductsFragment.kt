package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import sheridan.sharmupm.vegit_capstone.R

class ClassifyproductsFragment : Fragment() {

    private lateinit var classifyproductsViewModel: ClassifyproductsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        classifyproductsViewModel =
                ViewModelProvider(this).get(ClassifyproductsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_classifyproducts, container, false)

        
        return root
    }
}