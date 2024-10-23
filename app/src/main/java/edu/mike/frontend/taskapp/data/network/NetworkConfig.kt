package edu.mike.frontend.taskapp.data.network

object NetworkConfig {
    //  localhost in the emulator refers to the emulator itself, not the host machine.
    //  To access a service running on your development machine (the host) from the emulator,
    //  you need to use the special IP address 10.0.2.2.
    const val BASE_URL = "http://10.0.2.2:8080/v1/"
    //const val BASE_URL = "https://task-app-v01.herokuapp.com/v1/"
}