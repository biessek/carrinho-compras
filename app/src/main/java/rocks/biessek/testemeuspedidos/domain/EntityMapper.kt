package rocks.biessek.testemeuspedidos.domain

/**
 * Normalmente teria uma classe para converter entre as camadas data<-->domain
 */
interface EntityMapper<O, T> {
    fun convertTo(origin: O): T
    fun convertFrom(target: T): O
}