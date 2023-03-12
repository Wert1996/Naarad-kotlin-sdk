package com.naarad.naaradsdk.exceptions

class DeviceRegistrationException(message: String? = null, cause: Throwable? = null) :
    Exception("Error while registering device to Naarad: $message", cause)
