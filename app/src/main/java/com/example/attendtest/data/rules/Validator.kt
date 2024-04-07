package com.example.attendtest.data.rules

object Validator {

    fun validateFirstName(firstName: String): ValidationResult{
        return ValidationResult(
            (!firstName.isNullOrEmpty() && firstName.length <= 20)
        )
    }

    fun validateLastName(lastName: String): ValidationResult{
        return ValidationResult(
            (!lastName.isNullOrEmpty() && lastName.length <= 20)
        )
    }

    fun validateEmail(email: String): ValidationResult{
        return ValidationResult(
            (!email.isNullOrEmpty())
        )
    }

    fun validatePassword(password: String): ValidationResult{
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length >= 4)
        )
    }

    fun validatePolicyAcceptance(statusValue: Boolean): ValidationResult{
        return ValidationResult(
            statusValue
        )
    }
}

data class ValidationResult(
    val status: Boolean = false
)