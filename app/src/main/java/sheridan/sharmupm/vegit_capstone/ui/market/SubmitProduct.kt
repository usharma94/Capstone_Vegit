package sheridan.sharmupm.vegit_capstone.ui.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.market.SubmitProductViewModel
import sheridan.sharmupm.vegit_capstone.ui.market.stepper.*


class SubmitProduct : Fragment(), StepperFormListener {

    private lateinit var submitViewModel: SubmitProductViewModel
    private lateinit var nameStep: ProductNameStep
    private lateinit var imageStep: ProductImageStep
    private lateinit var dietStep: ProductDietTypeStep
    private lateinit var categoryStep: ProductCategoryTypeStep
    private lateinit var ingredientsStep: ProductIngredientStep
    private lateinit var stepperForm: VerticalStepperFormView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_product_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitViewModel = ViewModelProvider(this).get(SubmitProductViewModel::class.java)

        nameStep = ProductNameStep("Product Name")
        imageStep = ProductImageStep("Product Image")
        dietStep = ProductDietTypeStep("Diet Type")
        categoryStep = ProductCategoryTypeStep("Product Category")
        ingredientsStep = ProductIngredientStep("Product Ingredients")

        stepperForm = view.findViewById(R.id.stepper_form)
        stepperForm
            .setup(this, nameStep, imageStep, dietStep, categoryStep, ingredientsStep)
            .lastStepNextButtonText("Create Product")
            .init()

        submitViewModel.productResponse.observe(viewLifecycleOwner,
            { product ->
                if (product != null) {
                    showSubmitProductSuccessful()
                } else {
                    showSubmitProductFailed()
                }
            })
    }

    private fun showSubmitProductSuccessful() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.submit_product_success, Toast.LENGTH_LONG).show()
        this.findNavController().navigate(R.id.navigation_home)
    }

    private fun showSubmitProductFailed() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.submit_product_failed, Toast.LENGTH_LONG).show()
    }

    override fun onCompletedForm() {
        submitViewModel.submitProduct(nameStep.stepData, imageStep.stepData, dietStep.stepData, categoryStep.stepData, ingredientsStep.stepData)
    }

    override fun onCancelledForm() {
        stepperForm.cancelFormCompletionOrCancellationAttempt()
    }
}