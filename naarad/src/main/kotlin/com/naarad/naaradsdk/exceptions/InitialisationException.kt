package com.naarad.naaradsdk.exceptions

class InitialisationException(message: String? = null, cause: Throwable? = null) :
    Exception("Error while initialising Naarad: $message", cause)
