package xyz.ecys.simplemacro.data.repository

import xyz.ecys.simplemacro.data.dao.UserDao
import xyz.ecys.simplemacro.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository(private val userDao: UserDao) {
    
    fun getUserById(userId: Long): Flow<User?> = userDao.getUserById(userId)
    
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    suspend fun getCurrentUser(): User? = userDao.getCurrentUser()
}
