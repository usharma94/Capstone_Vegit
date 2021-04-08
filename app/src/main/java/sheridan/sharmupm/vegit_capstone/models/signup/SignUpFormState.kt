package sheridan.sharmupm.vegit_capstone.models.signup

data class SignUpFormState(
        val emailError: Int? = null,
        val passwordError: Int? = null,
        val firstNameError: Int? = null,
        val lastNameError: Int? = null,
        val isDataValid: Boolean = false
)