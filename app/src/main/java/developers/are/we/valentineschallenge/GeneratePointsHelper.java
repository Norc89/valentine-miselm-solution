package developers.are.we.valentineschallenge;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.Geometry;
import com.cocoahero.android.geojson.LineString;
import com.cocoahero.android.geojson.MultiLineString;
import com.cocoahero.android.geojson.Polygon;
import com.cocoahero.android.geojson.Position;
import com.cocoahero.android.geojson.Ring;

import org.json.JSONObject;

import java.util.List;

class GeneratePointsHelper {

    private static final double ARROW_LEFT_RIGHT_DISTANCE_RATION = 0.2f;
    private static final double ARROW_HEIGHT_DISTANCE_RATIO = 0.03125f;
    private static final double ARROW_BIG_HEIGHT_DISTANCE_RATIO = 2f;
    private static final double ONE_DEGREE_IN_KM = 110.57;
    private static final double KM_TO_METER = 0.001;
    private static final double FROM_METERS_TO_DEGREE_RATION = (1 / ONE_DEGREE_IN_KM) * KM_TO_METER;
    private static final String RED_COLOR = "#fe0101";
    private static final String BLACK_COLOR = "#000000";

    static FeatureCollection generateHeart(double latitude, double longitude, int radiusInMeters) {
        FeatureCollection featureCollection = new FeatureCollection();
        generateHeartPolygonAndArrow(featureCollection, latitude, longitude, radiusInMeters);
        return featureCollection;
    }

    private static void generateHeartPolygonAndArrow(FeatureCollection featureCollection, double latitude, double longitude, int radiusInMeters) {
        //Heart is drawn in points
        // x: 10 <> -10
        // y: -20 <> 20

        double ratio = 0.05 * (radiusInMeters * FROM_METERS_TO_DEGREE_RATION);
        Ring heartRing = new Ring();
        int res = 100;
        double r = 1;
        double theta = 0;
        for (int i = 0; i < res; ++i) {
            theta = 2 * Math.PI * i / (res - 1);
            double x = 13 * Math.cos(theta) - 5 * Math.cos(2 * theta) - 2 * Math.cos(2 * theta) - Math.cos(4 * theta);
            double y = 16 * Math.pow(r * Math.sin(theta), 3);
            Log.d("sdfds", "x:"+x+" y:"+y);
            addPositionToRing(heartRing, latitude + x * ratio, longitude + y * ratio);
        }
        List<Position> positionList = heartRing.getPositions();
        int heartPolygonSize = positionList.size();
        if (heartPolygonSize > 1) {
            Position startPoint = positionList.get((int) (heartPolygonSize * 0.3d));
            Position endPoint = positionList.get(((int) (heartPolygonSize * 0.7d)) - 1);
            Position bottomPoint = positionList.get(((int) (heartPolygonSize * 0.5d)));
            double distance = calculateDistance(startPoint, endPoint);
            addRingToFeatureCollection(featureCollection, drawPartOfArrow(endPoint, distance, false), new PropertyValuesBuilder().setFillColor(BLACK_COLOR).setFillOpacity(0.9f).Build());
            addRingToFeatureCollection(featureCollection, heartRing, new PropertyValuesBuilder().setFillColor(RED_COLOR).setFillOpacity(0.9f).Build());
            addRingToFeatureCollection(featureCollection, drawPartOfArrow(startPoint, distance, true), new PropertyValuesBuilder().setFillColor(BLACK_COLOR).setFillOpacity(0.9f).Build());
            drawText(featureCollection, bottomPoint, distance);
        }
    }

    private static Ring drawPartOfArrow(Position point, double distance, boolean rightArrow) {

        double arrowHeightDistance = distance * ARROW_HEIGHT_DISTANCE_RATIO;
        double arrowBigHeightDistance = arrowHeightDistance * ARROW_BIG_HEIGHT_DISTANCE_RATIO;
        Ring arrowRing = new Ring();
        if (!rightArrow) {
            double arrowLeftRightDistance = distance * ARROW_LEFT_RIGHT_DISTANCE_RATION;
            addPositionToRing(arrowRing, point.getLatitude() + arrowHeightDistance, point.getLongitude() + arrowLeftRightDistance / 10);
            addPositionToRing(arrowRing, point.getLatitude() + arrowHeightDistance, point.getLongitude() - arrowLeftRightDistance);

            addPositionToRing(arrowRing, point.getLatitude() + arrowBigHeightDistance, point.getLongitude() - arrowLeftRightDistance - arrowHeightDistance * 2d);

            addPositionToRing(arrowRing, point.getLatitude() + arrowBigHeightDistance, point.getLongitude() - arrowLeftRightDistance * 2.25d - arrowHeightDistance * 2d);
            addPositionToRing(arrowRing, point.getLatitude(), point.getLongitude() - arrowLeftRightDistance * 2.25d);
            addPositionToRing(arrowRing, point.getLatitude() - arrowBigHeightDistance, point.getLongitude() - arrowLeftRightDistance * 2.25d - arrowHeightDistance * 2d);

            addPositionToRing(arrowRing, point.getLatitude() - arrowBigHeightDistance, point.getLongitude() - arrowLeftRightDistance - arrowHeightDistance * 2d);

            addPositionToRing(arrowRing, point.getLatitude() - arrowHeightDistance, point.getLongitude() - arrowLeftRightDistance);
            addPositionToRing(arrowRing, point.getLatitude() - arrowHeightDistance, point.getLongitude() + arrowLeftRightDistance / 10);
        } else {
            double arrowLeftDistance = distance * 0.5;
            double arrowRightDistance = distance * ARROW_LEFT_RIGHT_DISTANCE_RATION;
            addPositionToRing(arrowRing, point.getLatitude() + arrowHeightDistance, point.getLongitude() - arrowLeftDistance);
            addPositionToRing(arrowRing, point.getLatitude() + arrowHeightDistance, point.getLongitude() + arrowRightDistance);
            addPositionToRing(arrowRing, point.getLatitude() + arrowBigHeightDistance, point.getLongitude() + arrowRightDistance);
            addPositionToRing(arrowRing, point.getLatitude(), point.getLongitude() + arrowRightDistance * 2d);
            addPositionToRing(arrowRing, point.getLatitude() - arrowBigHeightDistance, point.getLongitude() + arrowRightDistance);
            addPositionToRing(arrowRing, point.getLatitude() - arrowHeightDistance, point.getLongitude() + arrowRightDistance);
            addPositionToRing(arrowRing, point.getLatitude() - arrowHeightDistance, point.getLongitude() - arrowLeftDistance);
        }
        return arrowRing;
    }

    //‘Love You’
    private static void drawText(FeatureCollection featureCollection, Position point, double distance) {

        double fontSize = distance / 10;

        double latPos = point.getLatitude() - distance / 3;//y
        double lonPos = point.getLongitude() - distance / 2; //x

        MultiLineString multiLineString = new MultiLineString();

        //L
        multiLineString.addLineString(createLineString(new Position(latPos, lonPos + fontSize), new Position(latPos, lonPos), new Position(latPos + fontSize, lonPos)));
        lonPos += 1.5 * fontSize;
        //O
        multiLineString.addLineString(createLineString(new Position(latPos, lonPos + fontSize), new Position(latPos, lonPos), new Position(latPos + fontSize, lonPos), new Position(latPos + fontSize, lonPos + fontSize), new Position(latPos, lonPos + fontSize)));
        lonPos += 1.5 * fontSize;
        //V
        multiLineString.addLineString(createLineString(new Position(latPos + fontSize, lonPos), new Position(latPos, lonPos + fontSize / 2f), new Position(latPos + fontSize, lonPos + fontSize)));
        lonPos += 1.5 * fontSize;
        //E
        multiLineString.addLineString(createLineString(new Position(latPos + fontSize, lonPos + fontSize), new Position(latPos + fontSize, lonPos), new Position(latPos + fontSize / 2f, lonPos), new Position(latPos + fontSize / 2f, lonPos + fontSize), new Position(latPos + fontSize / 2f, lonPos), new Position(latPos, lonPos), new Position(latPos, lonPos + fontSize)));
        lonPos += 2 * fontSize;
        //Y
        multiLineString.addLineString(createLineString(new Position(latPos + fontSize, lonPos), new Position(latPos + fontSize / 2f, lonPos + fontSize / 2f), new Position(latPos + fontSize, lonPos + fontSize), new Position(latPos + fontSize / 2f, lonPos + fontSize / 2f), new Position(latPos, lonPos + fontSize / 2f)));
        lonPos += 1.5 * fontSize;
        //O
        multiLineString.addLineString(createLineString(new Position(latPos, lonPos + fontSize), new Position(latPos, lonPos), new Position(latPos + fontSize, lonPos), new Position(latPos + fontSize, lonPos + fontSize), new Position(latPos, lonPos + fontSize)));
        lonPos += 1.5 * fontSize;
        //U
        multiLineString.addLineString(createLineString(new Position(latPos + fontSize, lonPos), new Position(latPos, lonPos), new Position(latPos, lonPos + fontSize), new Position(latPos + fontSize, lonPos + fontSize)));

        addGeometryToFeatureCollection(multiLineString, new PropertyValuesBuilder().setFillColor(BLACK_COLOR).Build(), featureCollection);
    }

    private static LineString createLineString(Position... positions) {
        LineString lineString = new LineString();
        for (Position position : positions) {
            lineString.addPosition(position);
        }
        return lineString;
    }

    private static double calculateDistance(Position point1, Position point2) {
        double dx = point1.getLatitude() - point2.getLatitude();
        double dy = point1.getLongitude() - point2.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static void addPositionToRing(Ring ring, double latitude, double longitude) {
        ring.addPosition(new Position(latitude, longitude));
    }

    private static void addRingToFeatureCollection(FeatureCollection featureCollection, Ring ring, JSONObject properties) {

        Polygon polygon = new Polygon();
        polygon.addRing(ring);
        addGeometryToFeatureCollection(polygon, properties, featureCollection);
    }

    private static void addGeometryToFeatureCollection(Geometry geometry, JSONObject properties, FeatureCollection featureCollection) {
        Feature feature = new Feature();
        feature.setGeometry(geometry);
        if (properties != null) {
            feature.setProperties(properties);
        }
        featureCollection.addFeature(feature);
    }
}
