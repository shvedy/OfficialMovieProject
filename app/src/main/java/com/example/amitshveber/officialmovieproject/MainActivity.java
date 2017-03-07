package com.example.amitshveber.officialmovieproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {
    ImageButton plusBtn;
    ListView mainLV;
    mySql SQLTable;
    Cursor cursor;
    CursorAdapter cursorAdapter;

    @Override
    protected void onPostResume() {
        cursor = SQLTable.getReadableDatabase().query(DBContants.tableName, null, null, null, null, null, null);
        cursorAdapter.swapCursor(cursor);

        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLTable = new mySql(this);

        plusBtn = (ImageButton) findViewById(R.id.plusBtn);
        mainLV = (ListView) findViewById(R.id.mainLV);
        cursor = SQLTable.getReadableDatabase().query(DBContants.tableName, null, null, null, null, null, null);
        String[] from = new String[]{DBContants.titeColumm};
        int[] to = new int[]{R.id.movieItem};
        cursorAdapter = new android.widget.SimpleCursorAdapter(this, R.layout.movie_item, cursor, from, to);
        mainLV.setAdapter(cursorAdapter);
        mainLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = SQLTable.getReadableDatabase().query(DBContants.tableName, null, null, null, null, null, null);
                cursor.moveToPosition(position);
                String title = cursor.getString(cursor.getColumnIndex(DBContants.titeColumm));
                String description = cursor.getString(cursor.getColumnIndex(DBContants.descriptionColumm));
                String url = cursor.getString(cursor.getColumnIndex(DBContants.ImgColumm));
                String year=cursor.getString(cursor.getColumnIndex(DBContants.yearColumm));
                String rate=cursor.getString(cursor.getColumnIndex(DBContants.rateColumm));
                String runTime=cursor.getString(cursor.getColumnIndex(DBContants.RuntimeColumm));
                String actors=cursor.getString(cursor.getColumnIndex(DBContants.actorsColumm));
                Intent intent = new Intent(MainActivity.this, watchScreen.class);
                intent.putExtra("title", title);
                intent.putExtra("desc", description);
                intent.putExtra("url", url);
                intent.putExtra("year", year);
                intent.putExtra("rate", rate);
                intent.putExtra("runTime", runTime);
                intent.putExtra("actors", actors);

                startActivity(intent);

            }
        });
        mainLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                cursor.moveToPosition(position);
                final int current = cursor.getInt(cursor.getColumnIndex(DBContants.idColumm));
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.popuplongclick, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editPopupLong:
                                Intent intent = new Intent(MainActivity.this, editScreen.class);


                                intent.putExtra("title", cursor.getString(cursor.getColumnIndex(DBContants.titeColumm)));
                                intent.putExtra("description", cursor.getString(cursor.getColumnIndex(DBContants.descriptionColumm)));
                                intent.putExtra("url", cursor.getString(cursor.getColumnIndex(DBContants.ImgColumm)));
                                intent.putExtra("id", current);


                                DBContants.Edit = true;
                                startActivity(intent);

                                break;
                            case R.id.deletPopupLongClick:


                                AlertDialog.Builder alrtBuild = new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Are you sure you want to delete this movie from your list? ")
                                        .setTitle("HEY!")
                                        .setCancelable(true)
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String[] Current = new String[]{"" + current};
                                                SQLTable.getWritableDatabase().delete(DBContants.tableName, "_id=?", Current);
                                                cursor = SQLTable.getReadableDatabase().query(DBContants.tableName, null, null, null, null, null, null);
                                                cursorAdapter.swapCursor(cursor);
                                                finish();
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.cancel();
                                            }
                                        });

                                alrtBuild.create().show();


                                break;
                        }
                        return true;
                    }
                });
                popup.show();

                return true;
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, plusBtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.manualPopup:
                                Intent intent = new Intent(MainActivity.this, editScreen.class);
                                startActivity(intent);
                                break;
                            case R.id.webPopup:
                                Intent intent2 = new Intent(MainActivity.this, internetScreen.class);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quitOM:
                AlertDialog.Builder alrtBuild = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure you want to close the App? ")
                        .setTitle("HEY!")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                alrtBuild.create().show();
                break;
            case R.id.clearOM:
                AlertDialog.Builder alrtBuild1 = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure you want to Delet all your Data? ")
                        .setTitle("WARNING!!")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLTable.getWritableDatabase().delete(DBContants.tableName, null, null);
                                cursor = SQLTable.getReadableDatabase().query(DBContants.tableName, null, null, null, null, null, null);
                                cursorAdapter.swapCursor(cursor);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                alrtBuild1.create().show();

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        cursor=SQLTable.getReadableDatabase().query(DBContants.tableName,null,null,null,null,null,null);
        cursorAdapter.swapCursor(cursor);




        super.onResume();
    }
}
