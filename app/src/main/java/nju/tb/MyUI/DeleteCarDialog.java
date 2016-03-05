package nju.tb.MyUI;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import nju.tb.R;

public class DeleteCarDialog extends Dialog {
    private Context context;
    private TextView deleteTextView;
    private DeleteCar deleteCar;

    public interface DeleteCar {
        void deleteCar();
    }

    public DeleteCarDialog(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.view_cars_deletecar);
        deleteTextView = (TextView) findViewById(R.id.cars_deletecar);
        deleteTextView.setOnClickListener(new DialogOnClickListener());
    }

    public void setDeleteCar(DeleteCar deleteCar) {
        this.deleteCar = deleteCar;
    }

    private class DialogOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            deleteCar.deleteCar();
        }
    }
}
