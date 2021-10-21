package sheridan.sharmupm.vegit_capstone.models.products

data class SubmitProductFormState(
        val nameError: Int? = null,
        val dietError: Int? = null,
        val categoryError: Int? = null,
        val imgError: Int? = null,
        val ingredientError: Int? = null
)