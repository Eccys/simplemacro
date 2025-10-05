package xyz.ecys.simplemacro.util

/**
 * Input validation utilities to follow DRY principles.
 * Reusable validation logic for text fields across the app.
 */
object InputValidation {
    
    /**
     * Validates that input contains only digits (integers)
     * @param input The text to validate
     * @return true if valid or empty, false otherwise
     */
    fun isValidInteger(input: String): Boolean {
        return input.isEmpty() || input.all { it.isDigit() }
    }
    
    /**
     * Validates that input contains only digits and one optional decimal point
     * @param input The text to validate
     * @return true if valid decimal number or empty, false otherwise
     */
    fun isValidDecimal(input: String): Boolean {
        return input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))
    }
    
    /**
     * Validates that inches value is between 0-11
     * @param input The text to validate
     * @return true if valid inches (0-11) or empty, false otherwise
     */
    fun isValidInches(input: String): Boolean {
        if (input.isEmpty()) return true
        if (!input.all { it.isDigit() }) return false
        val value = input.toIntOrNull() ?: return false
        return value in 0..11
    }
    
    /**
     * Validates age input (positive integer, reasonable range)
     * @param input The text to validate
     * @return true if valid age or empty, false otherwise
     */
    fun isValidAge(input: String): Boolean {
        if (input.isEmpty()) return true
        if (!input.all { it.isDigit() }) return false
        val age = input.toIntOrNull() ?: return false
        return age in 1..150
    }
}
