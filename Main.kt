import java.lang.Thread.sleep
import java.time.Duration

data class Address(val datacenter: String, val nodeId: String)
data class Event(val recipients: List<Address>, val payload: Payload)
data class Payload(val origin: String, val data: ByteArray)

enum class Result { ACCEPTED, REJECTED }

interface Client {
    //блокирующий метод для чтения данных
    fun readData(): Event

    //блокирующий метод отправки данных
    fun sendData(dest: Address, payload: Payload): Result
}

interface Handler {
    val timeout: Duration

    fun performOperation(client: Client) {

        var readResult = client.readData()

        for (recipient in readResult.recipients) {

            var sendResult = client.sendData(recipient, readResult.payload)

            while (sendResult == Result.REJECTED) {
                    sleep(timeout.toMillis())
                    sendResult = client.sendData(recipient, readResult.payload)
            }


        }


    }
}

fun main() {
}