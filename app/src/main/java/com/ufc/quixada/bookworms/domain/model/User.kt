import java.util.Date

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val fotoUrl: String? = null,
    val turma: String? = null,
    val role: UserRole = UserRole.ALUNO,
    val dataCadastro: Date = Date()
)

enum class UserRole {
    ALUNO,
    DOCENTE
}