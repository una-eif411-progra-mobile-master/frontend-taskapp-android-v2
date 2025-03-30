package edu.mike.frontend.taskapp.data.repository

     import edu.mike.frontend.taskapp.data.mapper.TaskMapper
     import edu.mike.frontend.taskapp.data.remote.TaskRemoteDataSource
     import edu.mike.frontend.taskapp.domain.model.Task
     import edu.mike.frontend.taskapp.domain.repository.TaskRepository
     import javax.inject.Inject

     class TaskRepositoryImpl @Inject constructor(
         private val remoteDataSource: TaskRemoteDataSource,
         private val taskMapper: TaskMapper
     ) : TaskRepository {

         override suspend fun findAllTasks(): Result<List<Task>> {
             return remoteDataSource.getAllTasks().map { taskDto ->
                 taskMapper.mapToDomainList(taskDto)
             }
         }

         override suspend fun findTaskById(taskId: Long): Result<Task> {
             return remoteDataSource.getTaskById(taskId).map { taskDto ->
                 taskMapper.mapToDomain(taskDto)
             }
         }
     }