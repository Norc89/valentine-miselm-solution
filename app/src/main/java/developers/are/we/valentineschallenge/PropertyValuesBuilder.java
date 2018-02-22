package developers.are.we.valentineschallenge;

import org.json.JSONException;
import org.json.JSONObject;


class PropertyValuesBuilder {

    private JSONObject jsonObject = new JSONObject();
    PropertyValuesBuilder setFillColor(String fillColor) {
        setValue("fill", fillColor);
        return this;
    }

    PropertyValuesBuilder setFillOpacity(float fillOpacity) {
        setValue("fill-opacity", fillOpacity);
        return this;
    }

    JSONObject Build() {
        return jsonObject;
    }

    private void setValue(String key, Object object) {
        try {
            jsonObject.put(key, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
