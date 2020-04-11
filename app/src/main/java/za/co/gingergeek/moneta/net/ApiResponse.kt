package za.co.gingergeek.moneta.net

data class ApiResponse<T>(var value: T?, var successful: Boolean)