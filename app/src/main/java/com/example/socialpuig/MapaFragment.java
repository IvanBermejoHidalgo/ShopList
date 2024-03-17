package com.example.socialpuig;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.socialpuig.R;

public class MapaFragment extends Fragment {

    private ImageView imageView;
    private Button zoomInButton;
    private Button zoomOutButton;
    private Button saveButton;

    private Matrix matrix = new Matrix();
    private float minScale = 1f;
    private float maxScale = 3f;
    private float[] matrixValues = new float[9];
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF startPoint = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private static final float TOUCH_SENSITIVITY = 0.10f; // Ajusta este valor según sea necesario


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        imageView = view.findViewById(R.id.imageView);
        zoomInButton = view.findViewById(R.id.zoomInButton);
        zoomOutButton = view.findViewById(R.id.zoomOutButton);
        //saveButton = view.findViewById(R.id.saveButton);

        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        matrix.set(view.getImageMatrix());
                        startPoint.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            matrix.set(view.getImageMatrix());
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(view.getImageMatrix());
                            float dx = (event.getX() - startPoint.x) * TOUCH_SENSITIVITY; // Multiplica por el factor de escala
                            float dy = (event.getY() - startPoint.y) * TOUCH_SENSITIVITY; // Multiplica por el factor de escala
                            matrix.postTranslate(dx, dy);

                            // Limitar el movimiento dentro de los márgenes de la vista
                            float[] matrixValues = new float[9];
                            matrix.getValues(matrixValues);
                            float imageWidth = view.getDrawable().getIntrinsicWidth() * matrixValues[Matrix.MSCALE_X];
                            float imageHeight = view.getDrawable().getIntrinsicHeight() * matrixValues[Matrix.MSCALE_Y];
                            float viewWidth = view.getWidth();
                            float viewHeight = view.getHeight();
                            if (imageWidth <= viewWidth) {
                                matrixValues[Matrix.MTRANS_X] = 0;
                            } else {
                                matrixValues[Matrix.MTRANS_X] = Math.min(0, Math.max(matrixValues[Matrix.MTRANS_X], viewWidth - imageWidth));
                            }
                            if (imageHeight <= viewHeight) {
                                matrixValues[Matrix.MTRANS_Y] = 0;
                            } else {
                                matrixValues[Matrix.MTRANS_Y] = Math.min(0, Math.max(matrixValues[Matrix.MTRANS_Y], viewHeight - imageHeight));
                            }
                            matrix.setValues(matrixValues);
                        }else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(view.getImageMatrix());
                                float scale = newDist / oldDist;
                                matrix.getValues(matrixValues);
                                float currentScale = matrixValues[Matrix.MSCALE_X];
                                if (scale * currentScale < maxScale && scale * currentScale > minScale) {
                                    matrix.postScale(scale, scale, mid.x, mid.y);
                                }
                            }
                        }
                        break;
                }

                view.setImageMatrix(matrix);
                return true;
            }
        });

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matrix.getValues(matrixValues);
                float currentScale = matrixValues[Matrix.MSCALE_X];
                if (currentScale < maxScale) {
                    matrix.postScale(1.1f, 1.1f, v.getWidth() / 2, v.getHeight() / 2);
                    imageView.setImageMatrix(matrix);
                }
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matrix.getValues(matrixValues);
                float currentScale = matrixValues[Matrix.MSCALE_X];
                if (currentScale > minScale) {
                    matrix.postScale(0.9f, 0.9f, v.getWidth() / 2, v.getHeight() / 2);
                    imageView.setImageMatrix(matrix);
                }
            }
        });

        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar al fragmento crearEquipo
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.crearEquipo);
            }
        });*/

        return view;
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}