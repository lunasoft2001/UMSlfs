package at.ums.luna.umslfs.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.ums.luna.umslfs.R;
import at.ums.luna.umslfs.modelos.Clientes;

/**
 * Created by luna-aleixos on 09.06.2016.
 */
public class DialogoListaCampoClientes extends BaseAdapter{

    private ArrayList<String> listData;

    private LayoutInflater layoutInflater;

    public DialogoListaCampoClientes(Context context, ArrayList<String> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialogo_lista_clientes_items, null);
            holder = new ViewHolder();
            holder.campoResultante = (TextView) convertView.findViewById(R.id.campoResultante);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.campoResultante.setText(listData.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView campoResultante;
    }
}
