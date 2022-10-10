package com.example.mytodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
private FirebaseFirestore firebaseFirestore;
private RecyclerView recyclerview;
private FloatingActionButton floatingActionButton;
private DatabaseReference reference;
private FirebaseAuth mAuth;
private FirebaseUser mUser;
private String onlineUserId;

private ProgressDialog loader;

private String key = "";
private String uptask;
private String updes;
private String updue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         linearLayoutManager.setReverseLayout(true);
         linearLayoutManager.setStackFromEnd(true);
         recyclerview.setHasFixedSize(true);
         recyclerview.setLayoutManager(linearLayoutManager);
         loader = new ProgressDialog(this);
         mAuth = FirebaseAuth.getInstance();
         mUser = mAuth.getCurrentUser();
         onlineUserId = mUser.getUid();
         reference = FirebaseDatabase.getInstance().getReference().child("task").child(onlineUserId);




         floatingActionButton= findViewById(R.id.floatingActionButton);

         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addTask();
             }
         });
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setNegativeButton("No" , null)
                .show();
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file,null);
        myDialog.setView(myView);




        final AlertDialog dialog =  myDialog.create();
        dialog.setCancelable(false);
        dialog.show();
        Button cancel = myView.findViewById(R.id.CancelUpdate);
        Button confirm = myView.findViewById(R.id.ConfirmUpdate);
        final EditText taskfinal = myView.findViewById(R.id.Task);
        final EditText descriptionfinal = myView.findViewById(R.id.Description);
        final EditText duedate = myView.findViewById(R.id.editTextDate2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String mtask = taskfinal.getText().toString().trim();
                String mdescription= descriptionfinal.getText().toString().trim();
                String dueDate = duedate.getText().toString().trim();
                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());


                if(TextUtils.isEmpty(mtask)){
                 taskfinal.setError("Task is required");
                 return;
                }
                if(TextUtils.isEmpty(mdescription)){
                    descriptionfinal.setError("Description is required");
                    return;
                }
                else{
                    Toast.makeText(HomeActivity.this,dueDate,Toast.LENGTH_SHORT).show();
                    loader.setMessage("Adding your data ");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    Model model = new Model(mtask,mdescription,id,date,dueDate);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              Toast.makeText(HomeActivity.this,"Added succesfully",Toast.LENGTH_SHORT).show();
                              loader.dismiss();
                              dialog.dismiss();

                          }
                          else{
                              String error = task.getException().toString();
                              Toast.makeText(HomeActivity.this,error,Toast.LENGTH_SHORT).show();
                              loader.dismiss();
                              dialog.dismiss();
                          }
                        }
                    });
                }

            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(reference,Model.class)
                .build();
        FirebaseRecyclerAdapter<Model,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Model model) {
                holder.setTask(model.getTask());
                holder.setDate(model.getDate());
                holder.setDue(model.getDueDate());
                holder.setDesc(model.getDescription());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        uptask = model.getTask();
                        updes =model.getDescription();
                        updue = model.getDueDate();
                        updateTask();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrived_layout,parent,false);
                return new MyViewHolder(view);
            }
        };
          recyclerview.setAdapter(adapter);
          adapter.startListening();
    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflator  = LayoutInflater.from(this);
        View view = inflator.inflate(R.layout.update_task,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mtask = view.findViewById(R.id.TaskUpdate);
        EditText mdesc = view.findViewById(R.id.DescriptionUpdate);
        EditText mdue = view.findViewById(R.id.editTextDate2Update);

        Button updatebtn = view.findViewById(R.id.ConfirmUpdate);
        Button delbtn = view.findViewById(R.id.CancelUpdate);

        mtask.setText(uptask);
        mtask.setSelection(uptask.length());
        mdesc.setText(updes);
        mdesc.setSelection(updes.length());
        mdue.setText(updue);
        mdue.setSelection(updue.length());
        String date  = DateFormat.getDateInstance().format(new Date());
        dialog.show();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uptask = mtask.getText().toString().trim();
                updes = mdesc.getText().toString().trim();
                updue = mdue.getText().toString().trim();

                Model model = new Model(uptask,updes,key,date,updue);
                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(HomeActivity.this,"Task updated successfully",Toast.LENGTH_SHORT).show();

                    }

                    else{
                        String error = task.getException().toString();
                        Toast.makeText(HomeActivity.this,error,Toast.LENGTH_SHORT).show();
                    }
                    }
                });
                dialog.dismiss();


            }
        });

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this,"Task deleted successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String error = task.getException().toString();
                            Toast.makeText(HomeActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        ItemTouchHelper.SimpleCallback item = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView ;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }
    public void setTask(String task){
        TextView tasktextview  = mView.findViewById(R.id.taskrecyclerview);
        tasktextview.setText(task);

    }

    public void setDesc(String desc){
        TextView desctextview  = mView.findViewById(R.id.descriptionrecyclerview);
        desctextview.setText(desc);
    }


    public void setDate(String date){
        TextView datetextview  = mView.findViewById(R.id.daterecyclerview);
        datetextview.setText(date);
    }


    public void setDue(String due){
        TextView duetextview  = mView.findViewById(R.id.Duedaterecyclerview);
        duetextview.setText(due);
    }

}
}