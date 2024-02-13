package fastcampus.aop.pjt23_todo.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt23_todo.R
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.databinding.ViewholderTodoItemBinding

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.TodoItemViewHolder>() {

    private var todoList: List<TodoEntity> = listOf()
    private lateinit var todoItemClickListener: (TodoEntity) -> Unit
    private lateinit var todoCheckListener: (TodoEntity) -> Unit

    inner class TodoItemViewHolder(
        private val binding: ViewholderTodoItemBinding,
        val todoItemClickListener: (TodoEntity) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TodoEntity) = with(binding) {
            checkBox.text = data.title
            checkTodoComplete(data.hasCompleted)

            checkBox.setOnClickListener {
                todoCheckListener(
                    data.copy(hasCompleted = checkBox.isChecked)
                )
                checkTodoComplete(checkBox.isChecked)
            }

            root.setOnClickListener {
                todoItemClickListener(data)
            }
        }

        private fun checkTodoComplete(isChecked: Boolean) = with(binding) {
            checkBox.isChecked = isChecked
            container.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if (isChecked) {
                        R.color.gray_300
                    } else {
                        R.color.white
                    }
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder(ViewholderTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), todoItemClickListener)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount(): Int = todoList.size

    fun setTodoList(
        todoList: List<TodoEntity>,
        todoItemClickListener: (TodoEntity) -> Unit,
        todoCheckListener: (TodoEntity) -> Unit
    ) {
        this.todoList = todoList
        this.todoItemClickListener = todoItemClickListener
        this.todoCheckListener = todoCheckListener
        notifyDataSetChanged()
    }
}