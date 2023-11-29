package com.example.hambaapp

sealed class BusinessSetupStatus {
    data class SetupComplete(val isSetupComplete: Boolean, val errorMessage: String? = null) : BusinessSetupStatus()
    data class Error(val errorMessage: String) : BusinessSetupStatus()
}
//------------------------------------------ooo000EndOfFile000ooo-----------------------------------