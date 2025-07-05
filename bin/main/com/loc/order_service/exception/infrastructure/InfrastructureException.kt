package com.loc.order_service.exception.infrastructure


abstract class InfrastructureException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)


class ExternalServiceUnavailableException(
    serviceName: String,
    message: String = "External service $serviceName is currently unavailable",
    cause: Throwable? = null
) : InfrastructureException(message, cause)


class ExternalServiceTimeoutException(
    serviceName: String,
    message: String = "Timeout occurred while calling external service $serviceName",
    cause: Throwable? = null
) : InfrastructureException(message, cause)

class ExternalServiceClientException(
    serviceName: String,
    statusCode: Int,
    message: String = "Client error ($statusCode) from external service $serviceName",
    cause: Throwable? = null
) : InfrastructureException(message, cause)


class ExternalServiceServerException(
    serviceName: String,
    statusCode: Int,
    message: String = "Server error ($statusCode) from external service $serviceName",
    cause: Throwable? = null
) : InfrastructureException(message, cause)

class NetworkException(
    message: String,
    cause: Throwable? = null
) : InfrastructureException(message, cause)


// kafka connect exception
class KafkaPublishException(
    message: String = "Failed to publish message to Kafka",
    cause: Throwable? = null
) : InfrastructureException(message, cause)

// database
class DatabaseConnectionException(
    message: String = "Failed to connect to the database",
    cause: Throwable? = null
) : InfrastructureException(message, cause)


