package com.bms.user.utils;

/**
 * Created by FAJAR-NB on 07/06/2018.
 */

public interface MasterConstants {
    public static final int EMPTY_ID = -777;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final String EMPTY = "";
    public static final String ONE_SPACE = " ";

    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String HEADING1 = "heading1";
    public static final String HEADING2 = "heading2";
    public static final String ID	= "id = @1";
    public static final String SUBTYPEID	= "subtypeid";
    public static final String FUEL	= "fuel";
    public static final String CANON = "canon";
    public static final String ELE = "ele";
    public static final String NAME = "name";
    public static final String DESCR = "descr";
    public static final String DESC = "desc";
    public static final String DESCRIPTION = "description";
    public static final String ALT = "alt";
    public static final String TYPE = "type";
    public static final String EXTENSIONS = "extensions";
    public static final String CATEGORYID = "categoryid";
    public static final String POINTSOURCEID = "pointsourceid";
    public static final String ICONID = "iconid";
    public static final String MINZOOM = "minzoom";
    public static final String SHOW = "show";
    public static final String TRACKID = "trackid";
    public static final String SPEED = "speed";
    public static final String DATE = "date";
    public static final String STYLE = "style";
    public static final String CNT = "cnt";
    public static final String DISTANCE = "distance";
    public static final String DURATION = "duration";
    public static final String ACTIVITY = "activity";
    public static final String MAPID = "mapid";
    public static final String LABEL = "label";
    public static final String SYMBOLID = "symbolid";
    public static final String SYMBOL = "symbol";
    public static final String ADDRESS = "address";
    public static final String IP = "ip";
    public static final String HIDDEN = "hidden";
    public static final String ACTIVE = "active";
    public static final String NOCOMM = "nocomm";
    public static final String DEAD = "dead";
    public static final String ISACTIVE = "isactive";
    public static final String SYMID = "symid";
    public static final String IMAGE = "image";
    public static final String ROOMCHAT = "roomchat";
    public static final String CHAT = "chat";
    public static final String CHATACK = "sms_ack";
    public static final String ROOMCHATID = "roomchatid";
    public static final String ROOMCHATID2 = "romchat";
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String SENDER2 = "id_pengirim";
    public static final String RECEIVER2 = "id_penerima";
    public static final String SDATE = "send_date";
    public static final String RDATE = "receive_date";
    public static final String RDATE2 = "tangal";
    public static final String MSG2 = "isi_sms";
    public static final String ISREAD = "isread";
    public static final String MSG = "message";
    public static final String STATUS = "status_sms";
    public static final String SRC = "src";
    public static final String DST = "dst";
    public static final String ISMASTER = "ismaster";

    public static final String SKENARIOSRCID = "skenariosrcid";

    public static final String POINTS = "points";
    public static final String FOES = "foes";
    public static final String FRIENDS = "friends";
    public static final String IDENTITY = "identity";
    public static final String CATEGORY = "category";
    public static final String FRIEND_CATEGORY = "friend_category";
    public static final String TRACKS = "tracks";
    public static final String TRACKPOINTS = "trackpoints";
    public static final String DATA = "data";
    public static final String GEODATA_FILENAME = "/geodata.db";
    public static final String TRACK = "Track";
    public static final String PARAMS = "params";
    public static final String MAPS = "maps";
    public static final String TACTICAL_SYMBOL = "tactical_symbol";
    public static final String SKENARIO = "pskenario";
    public static final String SKENARIOPOINTS = "pskenariopoints";
    public static final String BMS_DATA = "bms_data";
    public static final String OPS_FILENAME = "/bms_ops.db";
    public static final String SMS_FILENAME = "/sms.db";

    public static final String UPDATE_POINTS = "pointid = @1";
    public static final String UPDATE_CATEGORY = "categoryid = @1";
    public static final String UPDATE_TRACKS = "trackid = @1";
    public static final String UPDATE_MAPS = "mapid = @1";
    public static final String UPDATE_FRIEND = "friendid = @1";
    public static final String FRIEND_ADDRESS = "address = @1";
    public static final String UPDATE_CHAT = "chatid = @1";
    public static final String UPDATE_CHAT_SEND_DATE = "send_date = @1";
    public static final String UPDATE_ROOMCHAT = "roomchatid = @1";
    public static final String UPDATE_SKENARIO = "pskenarioid = @1";
    public static final String UPDATE_CUACA = "id = @0";

    //SQL CREATE
    //New FTP Table
    public static final String SQL_CREATE_ftp = "CREATE TABLE 'ftp' (fileid	INTEGER NOT NULL PRIMARY KEY UNIQUE, filename VARCHAR, status INTEGER)";
    public static final String GET_fileid_ftp = "SELECT fileid FROM ftp";

    public static final String SQL_CREATE_points = "CREATE TABLE 'points' (pointid INTEGER NOT NULL PRIMARY KEY UNIQUE,name VARCHAR,descr VARCHAR,lat FLOAT DEFAULT '0',lon FLOAT DEFAULT '0',alt FLOAT DEFAULT '0',hidden INTEGER DEFAULT '0',categoryid INTEGER,pointsourceid INTEGER,iconid INTEGER DEFAULT NULL);";
    public static final String SQL_CREATE_tactical_symbol = "CREATE TABLE 'tactical_symbol' (symid INTEGER NOT NULL PRIMARY KEY UNIQUE, categoryid INTEGER, descr VARCHAR, image blob);";
    public static final String SQL_CREATE_category = "CREATE TABLE 'category' (categoryid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, hidden INTEGER DEFAULT '0', iconid INTEGER DEFAULT NULL, minzoom INTEGER DEFAULT '14');";
    public static final String SQL_CREATE_pointsource = "CREATE TABLE IF NOT EXISTS 'pointsource' (pointsourceid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR);";
    public static final String SQL_CREATE_tracks = "CREATE TABLE IF NOT EXISTS 'tracks' (trackid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_trackpoints = "CREATE TABLE IF NOT EXISTS 'trackpoints' (trackid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, alt FLOAT, speed FLOAT, date DATETIME);";
    public static final String SQL_CREATE_activity = "CREATE TABLE 'activity' (activityid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR);";
    public static final String SQL_CREATE_drop_activity = "DROP TABLE IF EXISTS 'activity';";
    public static final String SQL_CREATE_insert_activity = "INSERT INTO 'activity' (activityid, name) VALUES (%d, '%s');";
    public static final String SQL_CREATE_maps = "CREATE TABLE IF NOT EXISTS 'maps' (mapid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, type INTEGER, params VARCHAR)";
    public static final String SQL_CREATE_friends = "CREATE TABLE IF NOT EXISTS 'friends' (" +
            "friendid INTEGER NOT NULL PRIMARY KEY UNIQUE, address INTEGER, parentid INTEGER, categoryid INTEGER, " +
            "symbolid INTEGER NOT NULL DEFAULT '206', title VARCHAR, descr VARCHAR, hidden INTEGER DEFAULT '0', " +
            "fuel FLOAT DEFAULT '0', canon INTEGER DEFAULT '0', gun INTEGER DEFAULT '0', " +
            "temperature FLOAT DEFAULT '0', humidity FLOAT DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', " +
            "heading1 INTEGER DEFAULT '0', heading2 INTEGER DEFAULT '0', alt FLOAT DEFAULT '0', speed FLOAT DEFAULT '0', " +
            "status INTEGER DEFAULT '2', lastupdate DATETIME);";
    public static final String SQL_CREATE_friends_2 = "CREATE TABLE IF NOT EXISTS 'friends' (" +
            "friendid INTEGER NOT NULL PRIMARY KEY UNIQUE, address INTEGER, parentid INTEGER, categoryid INTEGER, " +
            "image BLOB NOT NULL, title VARCHAR, descr VARCHAR, hidden INTEGER DEFAULT '0', " +
            "fuel FLOAT DEFAULT '0', canon INTEGER DEFAULT '0', gun INTEGER DEFAULT '0', " +
            "temperature FLOAT DEFAULT '0', humidity FLOAT DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', " +
            "heading1 INTEGER DEFAULT '0', heading2 INTEGER DEFAULT '0', alt FLOAT DEFAULT '0', speed FLOAT DEFAULT '0', " +
            "status INTEGER DEFAULT '2', lastupdate DATETIME);";
    public static final String SQL_CREATE_friends_3 = "CREATE TABLE IF NOT EXISTS 'friends' (" +
            "friendid INTEGER NOT NULL PRIMARY KEY UNIQUE, address INTEGER, parentid INTEGER, categoryid INTEGER, " +
            "image BLOB, title VARCHAR, descr VARCHAR, hidden INTEGER DEFAULT '0', " +
            "fuel FLOAT DEFAULT '0', canon INTEGER DEFAULT '0', gun INTEGER DEFAULT '0', " +
            "temperature FLOAT DEFAULT '0', humidity FLOAT DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', " +
            "heading1 INTEGER DEFAULT '0', heading2 INTEGER DEFAULT '0', alt FLOAT DEFAULT '0', speed FLOAT DEFAULT '0', " +
            "status INTEGER DEFAULT '2', lastupdate DATETIME);";
    public static final String SQL_CREATE_friends_4 = "CREATE TABLE IF NOT EXISTS 'friends' (" +
            "friendid INTEGER NOT NULL PRIMARY KEY UNIQUE, address INTEGER, parentid INTEGER, subtypeid INTEGER, " +
            "title VARCHAR, descr VARCHAR, hidden INTEGER DEFAULT '0', " +
            "fuel FLOAT DEFAULT '0', canon INTEGER DEFAULT '0', gun INTEGER DEFAULT '0', " +
            "temperature FLOAT DEFAULT '0', humidity FLOAT DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', " +
            "heading1 INTEGER DEFAULT '0', heading2 INTEGER DEFAULT '0', alt FLOAT DEFAULT '0', speed FLOAT DEFAULT '0', " +
            "status INTEGER DEFAULT '2', lastupdate DATETIME);";
    public static final String SQL_CREATE_friends_5 = "CREATE TABLE IF NOT EXISTS 'friends' (" +
            "friendid INTEGER NOT NULL PRIMARY KEY UNIQUE, address INTEGER, parentid INTEGER, typeid INTEGER, " +
            "subtypeid INTEGER, title VARCHAR, descr VARCHAR, hidden INTEGER DEFAULT '0', " +
            "fuel FLOAT DEFAULT '0', canon INTEGER DEFAULT '0', gun INTEGER DEFAULT '0', " +
            "temperature FLOAT DEFAULT '0', humidity FLOAT DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', " +
            "heading1 INTEGER DEFAULT '0', heading2 INTEGER DEFAULT '0', alt FLOAT DEFAULT '0', speed FLOAT DEFAULT '0', " +
            "status INTEGER DEFAULT '2', lastupdate DATETIME);";
    //	public static final String SQL_CREATE_friendgroup = "CREATE TABLE IF NOT EXISTS 'friendgroup' (friendgroupid INTEGER NOT NULL PRIMARY KEY UNIQUE);";
    public static final String SQL_CREATE_foes = "CREATE TABLE IF NOT EXISTS 'foes' (foesid INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', type VARCHAR, label VARCHAR, symbolid INTEGER, hidden INTEGER DEFAULT '0', active INTEGER DEFAULT '1', dead INTEGER DEFAULT '0');";
    public static final String SQL_CREATE_foes_2 = "CREATE TABLE IF NOT EXISTS 'foes' (foeid INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', title VARCHAR, descr VARCHAR, symbolid INTEGER, flag INTEGER DEFAULT '0', datecreated DATETIME);";
    public static final String SQL_CREATE_psituasi = "CREATE TABLE IF NOT EXISTS 'psituasi' (psituasiid INTEGER NOT NULL PRIMARY KEY UNIQUE,name VARCHAR,descr VARCHAR,lat FLOAT DEFAULT '0',lon FLOAT DEFAULT '0',alt FLOAT DEFAULT '0',hidden INTEGER DEFAULT '0',categoryid INTEGER,pointsourceid INTEGER,iconid INTEGER DEFAULT NULL);";
    public static final String SQL_CREATE_smanuver = "CREATE TABLE IF NOT EXISTS 'smanuver' (smanuverid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_smanuverpoints = "CREATE TABLE IF NOT EXISTS 'smanuverpoints' (smanuverid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, alt FLOAT, speed FLOAT, date DATETIME);";
    public static final String SQL_CREATE_pkendali = "CREATE TABLE IF NOT EXISTS 'pkendali' (pkendaliid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_pkendalipoints = "CREATE TABLE IF NOT EXISTS 'pkendalipoints' (smanuverid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, date DATETIME);";
    //public static final String SQL_CREATE_pskenario = "CREATE TABLE IF NOT EXISTS 'pskenario' (pskenarioid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_pskenario = "CREATE TABLE IF NOT EXISTS 'pskenario' (pskenarioid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR, skenariosrcid INTEGER);";
    public static final String SQL_CREATE_pskenariopoints = "CREATE TABLE IF NOT EXISTS 'pskenariopoints' (pskenarioid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, alt FLOAT, speed FLOAT, date DATETIME);";
    public static final String SQL_CREATE_sobyek = "CREATE TABLE IF NOT EXISTS 'sobyek' (sobyekid INTEGER NOT NULL PRIMARY KEY UNIQUE,name VARCHAR,descr VARCHAR,lat FLOAT DEFAULT '0',lon FLOAT DEFAULT '0',alt FLOAT DEFAULT '0',hidden INTEGER DEFAULT '0',categoryid INTEGER,pointsourceid INTEGER,iconid INTEGER DEFAULT NULL);";
    public static final String SQL_CREATE_smarka = "CREATE TABLE IF NOT EXISTS 'smarka' (smarkaid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_smarkapoints = "CREATE TABLE IF NOT EXISTS 'smarkapoints' (smarkaid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, alt FLOAT, speed FLOAT, date DATETIME);";
    public static final String SQL_CREATE_cuaca = "CREATE TABLE IF NOT EXISTS 'cuaca' (id INTEGER NOT NULL PRIMARY KEY UNIQUE, suhu FLOAT DEFAULT '0', kelembaban FLOAT DEFAULT '0', hujan FLOAT DEFAULT '0', kabut FLOAT DEFAULT '0', angin FLOAT DEFAULT '0');";
    public static final String SQL_CREATE_roomchat = "CREATE TABLE IF NOT EXISTS 'roomchat' (roomchatid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR NOT NULL UNIQUE, src INTEGER, dst INTEGER, ismaster INTEGER);";
    public static final String SQL_CREATE_chat = "CREATE TABLE IF NOT EXISTS 'chat' (chatid INTEGER NOT NULL PRIMARY KEY UNIQUE, roomchatid INTEGER NOT NULL, sender VARCHAR NOT NULL, receiver VARCHAR NOT NULL, send_date DATETIME, receive_date DATETIME, isread INTEGER DEFAULT '0', message VARCHAR);";
    public static final String SQL_CREATE_chatack = "CREATE TABLE `sms_ack` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `id_pengirim` INTEGER, `id_penerima` INTEGER, `romchat` TEXT, `tangal` TEXT, `isi_sms` TEXT );";
    public static final String SQL_CREATE_kendali = "CREATE TABLE IF NOT EXISTS 'kendali' (kendaliid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, descr VARCHAR, date DATETIME, show INTEGER, cnt INTEGER, duration INTEGER, distance INTEGER, categoryid INTEGER, activity INTEGER, style VARCHAR);";
    public static final String SQL_CREATE_kendalipoints = "CREATE TABLE IF NOT EXISTS 'kendalipoints' (kendaliid INTEGER NOT NULL, id INTEGER NOT NULL PRIMARY KEY UNIQUE, lat FLOAT, lon FLOAT, alt FLOAT, speed FLOAT, date DATETIME);";
    public static final String SQL_CREATE_identity = "CREATE TABLE IF NOT EXISTS 'identity' (id INTEGER NOT NULL PRIMARY KEY UNIQUE, role INTEGER NOT NULL, mmi_ip VARCHAR NOT NULL, ni_ip VARCHAR NOT NULL, server_port VARCHAR NOT NULL DEFAULT '8019', title VARCHAR, descr VARCHAR);";
    public static final String SQL_CREATE_manual_ops = "CREATE TABLE IF NOT EXISTS 'manual_ops' (id INTEGER NOT NULL PRIMARY KEY UNIQUE, ismanloc INTEGER NOT NULL DEFAULT '0', lat FLOAT DEFAULT '0', lon FLOAT DEFAULT '0', ismanhtank INTEGER NOT NULL DEFAULT '0', htank INTEGER DEFAULT '0', ismanhkanon INTEGER NOT NULL DEFAULT '0', hkanon INTEGER DEFAULT '0', ismanfuel INTEGER NOT NULL DEFAULT '0', fuel FLOAT DEFAULT '0', ismanmunisikanon INTEGER NOT NULL DEFAULT '0', munisikanon INTEGER DEFAULT '0', ismanmunisigun INTEGER NOT NULL DEFAULT '0', munisigun INTEGER DEFAULT '0', ismansuhu INTEGER NOT NULL DEFAULT '0', suhu FLOAT DEFAULT '0', ismankelembaban INTEGER NOT NULL DEFAULT '0', kelembaban FLOAT DEFAULT '0');";
    //	public static final String SQL_CREATE_friend_category = "CREATE TABLE IF NOT EXISTS 'friend_category' (categoryid INTEGER NOT NULL PRIMARY KEY UNIQUE, name VARCHAR, hidden INTEGER DEFAULT '0', iconid INTEGER DEFAULT NULL, minzoom INTEGER DEFAULT '14');";
    public static final String SQL_CREATE_type = "CREATE TABLE 'type' (typeid INTEGER PRIMARY KEY  NOT NULL , name VARCHAR);";
    public static final String SQL_CREATE_subtype = "CREATE TABLE 'subtype' (subtypeid INTEGER PRIMARY KEY  NOT NULL, typeid INTEGER NOT NULL, name VARCHAR, image BLOB, hascannon INTEGER NOT NULL DEFAULT '0' );";
//	public static final String SQL_CREATE_satuan = "CREATE TABLE "Satuan" ("ID" INT PRIMARY KEY  NOT NULL  DEFAULT (null) ,"Type" INT NOT NULL ,"Name" TEXT,"SubTypeID" INT DEFAULT (null) ,"Image" blob)"
    // ----------------

    //SQL POINTS
//	public static final String STAT_GET_POI_LIST = "SELECT lat, lon, points.name, descr, pointid, pointid _id, pointid ID, points.iconid, category.name || ', ' || lat || ' ' || lon poititle2 FROM points LEFT JOIN category ON category.categoryid = points.categoryid ORDER BY lat, lon";
    public static final String STAT_GET_POI_LIST = "SELECT lat, lon, points.name, points.descr, pointid, pointid _id, pointid ID, points.iconid, category.name || ', ' || lat || ' ' || lon poititle2, ts.image FROM points LEFT JOIN category ON category.categoryid = points.categoryid LEFT JOIN tactical_symbol ts ON ts.symid = points.iconid ORDER BY pointid DESC";
    //	public static final String STAT_PoiListNotHidden = "SELECT poi.lat, poi.lon, poi.name, poi.descr, poi.pointid, poi.pointid _id, poi.pointid ID, poi.categoryid, cat.iconid FROM points poi LEFT JOIN category cat ON cat.categoryid = poi.categoryid WHERE poi.hidden = 0 AND cat.hidden = 0 "
//	+"AND cat.minzoom <= @1"
//	+ " AND poi.lon BETWEEN @2 AND @3"
//	+ " AND poi.lat BETWEEN @4 AND @5"
//	+ " ORDER BY lat, lon";
    public static final String STAT_PoiListNotHidden = "SELECT poi.lat, poi.lon, poi.name, poi.descr, poi.pointid, poi.pointid _id, poi.pointid ID, poi.categoryid, poi.iconid FROM points poi LEFT JOIN category cat ON cat.categoryid = poi.categoryid WHERE poi.hidden = 0 AND cat.hidden = 0 "
            +"AND cat.minzoom <= @1"
            + " AND poi.lon BETWEEN @2 AND @3"
            + " AND poi.lat BETWEEN @4 AND @5"
            + " ORDER BY lat, lon";
    public static final String STAT_getPoi = "SELECT lat, lon, name, descr, pointid, alt, hidden, categoryid, pointsourceid, iconid FROM points WHERE pointid = @1";
    public static final String STAT_getPoiSrcId = "SELECT lat, lon, name, descr, pointid, alt, hidden, categoryid, pointsourceid, iconid FROM points WHERE pointsourceid = @9";
    public static final String STAT_getSknSrcId = "SELECT name, descr, show, cnt, distance, duration, categoryid, activity, date, style, skenariosrcid FROM pskenario WHERE skenariosrcid = @12";
    public static final String STAT_PoiCategoryList = "SELECT name, iconid, categoryid _id FROM category ORDER BY name";
    public static final String STAT_FriendCategoryList = "SELECT name, iconid, categoryid _id FROM friend_category ORDER BY name";
    public static final String STAT_deletePoi = "DELETE FROM points WHERE pointid = @1";
    public static final String STAT_deletePoiSrcId = "DELETE FROM points WHERE pointsourceid = @9";
    public static final String STAT_deleteSknSrcId = "DELETE FROM pskenario WHERE skenariosrcid = @12";
    public static final String STAT_DeleteAllPoi = "DELETE FROM points";
    public static final String STAT_GET_FOE_LIST = "SELECT lat, lon, points.name || ' (' || points.descr || ')' foetitle, points.descr, pointid, pointid _id, pointid ID, points.iconid, lat || ', ' || lon foeititle2, ts.image FROM points LEFT JOIN category ON category.categoryid = points.categoryid LEFT JOIN tactical_symbol ts ON ts.symid = points.iconid WHERE points.iconid > 18000 AND points.iconid < 39000 ORDER BY pointid DESC";
    public static final String STAT_deletePoiCategory = "DELETE FROM category WHERE categoryid = @1";
    public static final String STAT_getPoiCategory = "SELECT name, categoryid, hidden, iconid, minzoom FROM category WHERE categoryid = @1";
    public static final String STAT_DeleteAllFoe = "DELETE FROM points WHERE points.iconid > 120 AND points.iconid < 155";
    public static final String STAT_getFriendCategory = "SELECT name, categoryid, hidden, iconid, minzoom FROM friend_category WHERE categoryid = @1";
    public static final String STAT_getFriend = "SELECT a.friendid, a.address, a.parentid, a.title, a.descr, a.typeid, a.subtypeid, b.image FROM friends a LEFT JOIN subtype b ON a.subtypeid = b.subtypeid WHERE friendid = @1";
    public static final String STAT_getFriendByAddress = "SELECT a.friendid, a.address, a.parentid, a.title, a.descr, a.hidden, a.typeid, a.subtypeid, b.image, a.lat, a.lon FROM friends a LEFT JOIN subtype b ON a.subtypeid = b.subtypeid WHERE address = @1";
    public static final String STAT_getFriendType = "SELECT * FROM friend_type";
    public static final String STAT_getFriendSubtype = "SELECT * FROM friend_subtype";
    public static final String STAT_DeleteAllType = "DELETE FROM type";
    public static final String STAT_DeleteAllSubType = "DELETE FROM subtype";
    public static final String STAT_getSubtype = "SELECT b.subtypeid FROM identity a LEFT JOIN subtype b ON a.subtypeid = b.subtypeid";
    // ----------------

    //SQL FRIENDS
//	public static final String STAT_GET_FRIEND_LIST = "SELECT *, friendid _id, friendid ID from friends";
    public static final String STAT_GET_FRIEND_LIST = "SELECT lat, lon, a.title, a.descr, friendid, friendid _id, friendid ID, b.name subtype, b.image FROM friends a LEFT JOIN subtype b ON b.subtypeid = a.subtypeid ORDER BY friendid DESC";
    public static final String STAT_GET_FRIEND_LIST2 = "SELECT lat, lon, a.title, a.descr, friendid, address _id, address ID, b.name subtype, b.image, a.status AS status FROM friends a LEFT JOIN subtype b ON b.subtypeid = a.subtypeid ORDER BY friendid DESC";
    //	public static final String STAT_FriendListNotHidden = "SELECT friend.lat, friend.lon, friend.title, friend.descr, friend.friendid, friend.friendid _id, friend.friendid ID, friend.categoryid, friend.symbolid FROM friends friend LEFT JOIN category cat ON cat.categoryid = friend.categoryid WHERE friend.hidden = 0 AND cat.hidden = 0 "
//			+"AND cat.minzoom <= @1"
//			+ " AND friend.lon BETWEEN @2 AND @3"
//			+ " AND friend.lat BETWEEN @4 AND @5"
//			+ " ORDER BY lat, lon";
    public static final String STAT_FriendListNotHidden = "SELECT friend.lat, friend.lon, friend.title, friend.descr, friend.friendid, friend.friendid _id, friend.friendid ID, friend.typeid, friend.subtypeid, cat.image, friend.heading1, friend.heading2, cat.hascannon FROM friends friend LEFT JOIN subtype cat ON cat.subtypeid = friend.subtypeid WHERE friend.hidden = 0 "
            + " AND friend.lon BETWEEN @1 AND @2"
            + " AND friend.lat BETWEEN @3 AND @4"
            + " ORDER BY lat, lon";
    public static final String STAT_getFriend2 = "SELECT lat, lon, heading1, heading2, title, descr, friendid, alt, speed, hidden, status, lastupdate, " +
            "friendtype, image, fuel, canon, gun, temperature, humidity FROM friends WHERE ";

    public static final String STAT_GridxList = "SELECT gridid, hundreds, gridx, lon FROM lcogrid_x WHERE"
            + " lon BETWEEN @1 AND @2"
            + " ORDER BY lon";
    public static final String STAT_GridyList = "SELECT gridid, hundreds, gridy, lat FROM lcogrid_y WHERE"
            + " lat BETWEEN @1 AND @2"
            + " ORDER BY lat";
    public static final String STAT_GET_MYIDENTITY = "SELECT a.*, b.name, b.image, b.hascannon FROM identity a LEFT JOIN subtype b ON a.subtypeid = b.subtypeid LIMIT 1";
    public static final String STAT_GET_CUACA = "SELECT * FROM cuaca";
    public static final String STAT_GET_POI = "SELECT * FROM points";
    public static final String STAT_GET_MYOVERLAY = "SELECT a.id, a.title, b.image, b.hascannon, b.subtypeid FROM identity a LEFT JOIN subtype b ON a.subtypeid = b.subtypeid LIMIT 1";
    public static final String STAT_GET_ROOMCHAT_LIST = "SELECT roomchatid, roomchatid _id, name, src, dst, ismaster FROM roomchat";
    public static final String STAT_ChatByRoomchatid = "SELECT chatid _id, roomchatid, sender, receiver, send_date, receive_date, isread, message FROM chat WHERE roomchatid = @1";
    //public static final String STAT_Chat_10 = "SELECT chatid _id, roomchatid, sender, receiver, send_date, receive_date, isread, message FROM chat ORDER BY chatid";
    public static final String STAT_Chat_10 = "SELECT chatid _id, roomchatid, sender, receiver, send_date, receive_date, isread, message FROM (SELECT * FROM chat ORDER BY chatid DESC LIMIT 10) ORDER BY chatid";
    public static final String STAT_UpdateChatIsRead = "UPDATE chat SET isread = 1 WHERE roomchatid = @1";
    public static final String STAT_FriendListActive = "SELECT lat, lon, type, label, id, id _id, id ID, symbol FROM friend WHERE isactive = 1";
    public static final String STAT_ActivityList = "SELECT name, activityid _id FROM activity ORDER BY activityid";
    public static final String STAT_deleteFriend = "DELETE FROM friends WHERE friendid = @1";
    public static final String STAT_DeleteAllSkenario = "DELETE FROM pskenario";
    public static final String STAT_DeleteAllSkenarioPoints = "DELETE FROM pskenariopoints";
    public static final String STAT_DeleteAllFriend = "DELETE FROM friends";
    public static final String STAT_TruncateFoes = "DELETE FROM foes";
    public static final String STAT_DeleteAllCuaca = "DELETE FROM cuaca";
    public static final String STAT_getTrackList = "SELECT tracks.name, activity.name || ', ' || strftime('%%d/%%m/%%Y %%H:%%M:%%S', date, 'unixepoch', 'localtime') As title2, descr, trackid _id, cnt, TIME('2011-01-01', duration || ' seconds') as duration, round(distance/1000, 2) AS distance0, show, IFNULL(duration, -1) As NeedStatUpdate, '%s' as units, round(distance/1000/1.609344, 2) AS distance1 FROM tracks LEFT JOIN activity ON activity.activityid = tracks.activity ORDER BY trackid DESC;";
    public static final String STAT_getSkenarioList = "SELECT pskenario.name, activity.name || ', ' || strftime('%%d/%%m/%%Y %%H:%%M:%%S', date, 'unixepoch', 'localtime') As title2, descr, pskenarioid _id, cnt, TIME('2011-01-01', duration || ' seconds') as duration, round(distance/1000, 2) AS distance0, show, IFNULL(duration, -1) As NeedStatUpdate, '%s' as units, round(distance/1000/1.609344, 2) AS distance1 FROM pskenario LEFT JOIN activity ON activity.activityid = pskenario.activity ORDER BY pskenarioid DESC;";
    public static final String STAT_getTrackChecked = "SELECT name, descr, show, trackid, cnt, distance, duration, categoryid, activity, date, style FROM tracks WHERE show = 1";
    public static final String STAT_getTrack = "SELECT name, descr, show, cnt, distance, duration, categoryid, activity, date, style FROM tracks WHERE trackid = @1";
    public static final String STAT_getSkenario = "SELECT name, descr, show, cnt, distance, duration, categoryid, activity, date, style, skenariosrcid FROM pskenario WHERE pskenarioid = @1";
    public static final String STAT_getTrackPoints = "SELECT lat, lon, alt, speed, date FROM trackpoints WHERE trackid = @1 ORDER BY id";
    public static final String STAT_getTP = "SELECT lat, lon, alt, speed, date FROM trackpoints ORDER BY id";
    public static final String STAT_setTrackChecked_1 = "UPDATE tracks SET show = 1 - show * 1 WHERE trackid = @1";
    public static final String STAT_setTrackChecked_2 = "UPDATE tracks SET show = 0 WHERE trackid <> @1";
    public static final String STAT_deleteTrack_1 = "DELETE FROM trackpoints WHERE trackid = @1";
    public static final String STAT_deleteTrack_2 = "DELETE FROM tracks WHERE trackid = @1";
    public static final String STAT_deleteSkenario_1 = "DELETE FROM pskenariopoints WHERE pskenarioid = @1";
    public static final String STAT_deleteSkenario_2 = "DELETE FROM pskenario WHERE pskenarioid = @1";
    public static final String STAT_saveTrackFromWriter = "SELECT lat, lon, alt, speed, date FROM trackpoints ORDER BY id;";
    public static final String STAT_CLEAR_TRACKPOINTS = "DELETE FROM 'trackpoints';";
    public static final String STAT_get_maps = "SELECT mapid, name, type, params FROM 'maps';";
    public static final String STAT_get_map = "SELECT mapid, name, type, params FROM 'maps' WHERE mapid = @1;";
    public static final String STAT_deleteChat = "DELETE FROM chat WHERE roomchatid = @1";
    public static final String STAT_deleteRoomchat = "DELETE FROM roomchat WHERE roomchatid = @1";
    public static final String STAT_getKendaliList = "SELECT kendali.name, activity.name || ', ' || strftime('%%d/%%m/%%Y %%H:%%M:%%S', date, 'unixepoch', 'localtime') As title2, descr, kendaliid _id, cnt, TIME('2011-01-01', duration || ' seconds') as duration, round(distance/1000, 2) AS distance0, show, IFNULL(duration, -1) As NeedStatUpdate, '%s' as units, round(distance/1000/1.609344, 2) AS distance1 FROM kendali LEFT JOIN activity ON activity.activityid = kendali.activity ORDER BY kendaliid DESC;";
    public static final String STAT_getKendaliChecked = "SELECT name, descr, show, kendaliid, cnt, distance, duration, categoryid, activity, date, style FROM kendali WHERE show = 1";
    public static final String STAT_getKendali = "SELECT name, descr, show, cnt, distance, duration, categoryid, activity, date, style FROM kendali WHERE kendaliid = @1";
    public static final String STAT_getKendaliPoints = "SELECT lat, lon, alt, speed, date FROM kendalipoints WHERE kendaliid = @1 ORDER BY id";
    public static final String STAT_getKP = "SELECT lat, lon, alt, speed, date FROM kendalipoints ORDER BY id";
    public static final String STAT_setKendaliChecked_1 = "UPDATE kendali SET show = 1 - show * 1 WHERE kendaliid = @1";
    public static final String STAT_setKendaliChecked_2 = "UPDATE kendali SET show = 0 WHERE kendaliid <> @1";
    public static final String STAT_deleteKendali_1 = "DELETE FROM kendalipoints WHERE kendaliid = @1";
    public static final String STAT_deleteKendali_2 = "DELETE FROM kendali WHERE kendaliid = @1";
    public static final String STAT_getSkenarioPoints = "SELECT lat, lon, alt, speed, date FROM pskenariopoints WHERE pskenarioid = @1 ORDER BY id";
    public static final String STAT_getSkenarioChecked = "SELECT name, descr, show, pskenarioid, cnt, distance, duration, categoryid, activity, date, style, skenariosrcid FROM pskenario WHERE show = 1";
    public static final String STAT_getTacticalSymbol = "SELECT symid, symid _id, categoryid, descr, image FROM tactical_symbol";
    public static final String STAT_getTacticalSymbolCategory = "SELECT symid, symid _id, categoryid, descr, image FROM tactical_symbol WHERE categoryid = @1 ORDER BY categoryid DESC";
    public static final String STAT_getSmsACK = "SELECT id _id, isi_sms,id_pengirim,tangal FROM sms_ack WHERE tangal = @1 ORDER BY id DESC";
    public static final String STAT_getAllSMS = "SELECT chatid _id, message , send_date , sender , receiver FROM chat ORDER BY chatid DESC";
    public static final String STAT_getFriendTacticalSymbolCategory = "SELECT symid, symid _id, categoryid, descr, image FROM tactical_symbol WHERE categoryid = @1 OR categoryid = 4 ORDER BY categoryid DESC";
    public static final String STAT_getTacticalSymbolSymid = "SELECT symid, symid _id, categoryid, descr, image FROM tactical_symbol WHERE symid = @1";
    public static final String STAT_getChat = "SELECT chatid _id, chatid, sender, receiver, send_date, receive_date, isread, message FROM chat WHERE roomchatid = @1";
    public static final String STAT_getRoomChat = "SELECT roomchatid, name, src, dst, ismaster FROM roomchat WHERE roomchatid = @1";
    public static final String STAT_updateSymbolMsh = "UPDATE points SET iconid = @2 WHERE pointid = @1";
    public static final String STAT_GET_manual_ops = "SELECT * FROM manual_ops";
    public static final String STAT_updateFriendStatus = "UPDATE friends SET status = @2 WHERE address = @1";

    public static final String INS_DEFAULT_IDENTITY = "INSERT INTO identity VALUES (1, 0, '0', '0', '8019', '-', '-');";
    public static final String INS_DEFAULT_MANUAL_OPS = "INSERT INTO manual_ops (id) VALUES (1);";
    public static final String INS_PSKENARIO = "INSERT INTO pskenario VALUES (null, 'Skenario 1', 'Skenario utama untuk kavaleri', '2013-7-3 22:36:51', 1, 20, 0, 0, 0, 0, '');";
    public static final String INS_PSKENARIOPOINTS = "";

    public static final String SQL_ADD_category = "";
    public static final String SQL_ADD_category_tank = "INSERT INTO 'friend_category' (categoryid, name, hidden, iconid) VALUES (1, 'Tank', 0, 206);";
    public static final String SQL_ADD_category_troop = "INSERT INTO 'friend_category' (categoryid, name, hidden, iconid) VALUES (2, 'Troop', 0, 208);";

    public static final String SQL_UPDATE_1_1 = "DROP TABLE IF EXISTS 'points_45392250'; ";
    public static final String SQL_UPDATE_1_2 = "CREATE TABLE 'points_45392250' AS SELECT * FROM 'points';";
    public static final String SQL_UPDATE_1_3 = "DROP TABLE 'points';";
    public static final String SQL_UPDATE_1_5 = "INSERT INTO 'points' (pointid, name, descr, lat, lon, alt, hidden, categoryid, pointsourceid, iconid) SELECT pointid, name, descr, lat, lon, alt, hidden, categoryid, pointsourceid";
    public static final String SQL_UPDATE_1_6 = "DROP TABLE 'points_45392250';";

    public static final String SQL_UPDATE_1_7 = "DROP TABLE IF EXISTS 'category_46134312'; ";
    public static final String SQL_UPDATE_1_8 = "CREATE TABLE 'category_46134312' AS SELECT * FROM 'category';";
    public static final String SQL_UPDATE_1_9 = "DROP TABLE 'category';";
    public static final String SQL_UPDATE_1_11 = "INSERT INTO 'category' (categoryid, name) SELECT categoryid, name FROM 'category_46134312';";
    public static final String SQL_UPDATE_1_12 = "DROP TABLE 'category_46134312';";

    public static final String SQL_UPDATE_2_7 = "DROP TABLE IF EXISTS 'category_46134313'; ";
    public static final String SQL_UPDATE_2_8 = "CREATE TABLE 'category_46134313' AS SELECT * FROM 'category';";
    public static final String SQL_UPDATE_2_9 = "DROP TABLE 'category';";
    public static final String SQL_UPDATE_2_11 = "INSERT INTO 'category' (categoryid, name, hidden, iconid) SELECT categoryid, name, hidden, iconid FROM 'category_46134313';";
    public static final String SQL_UPDATE_2_12 = "DROP TABLE 'category_46134313';";

    public static final String SQL_UPDATE_6_1 = "DROP TABLE IF EXISTS 'tracks_46134313'; ";
    public static final String SQL_UPDATE_6_2 = "CREATE TABLE 'tracks_46134313' AS SELECT * FROM 'tracks'; ";
    public static final String SQL_UPDATE_6_3 = "DROP TABLE IF EXISTS 'tracks'; ";
    public static final String SQL_UPDATE_6_4 = "INSERT INTO 'tracks' (trackid, name, descr, date, show, cnt, duration, distance, categoryid, activity) SELECT trackid, name, descr, date, show, (SELECT COUNT(*) FROM trackpoints WHERE trackid = tracks_46134313.trackid), null, null, null, 0 FROM 'tracks_46134313';";
    public static final String SQL_UPDATE_6_5 = "DROP TABLE 'tracks_46134313';";

    public static final String SQL_UPDATE_20_1 = "INSERT INTO 'tracks' (trackid, name, descr, date, show, cnt, duration, distance, categoryid, activity, style) SELECT trackid, name, descr, date, show, cnt, duration, distance, categoryid, activity, '' FROM 'tracks_46134313';";
    public static final String SQL_INSERT_FRIENDS_2 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (2,2,null, 1,1,'Tank 2','Tank 2',-6.949559,107.6282);";
    public static final String SQL_INSERT_FRIENDS_3 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (3,3,2, 1,1,'Tank 3','Tank 3',-6.949261,107.622707);";
    public static final String SQL_INSERT_FRIENDS_4 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (4,4,2, 1,1,'Tank 4','Tank 4',-6.94892,107.6176);";
    public static final String SQL_INSERT_FRIENDS_5 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (5,5,2, 1,1,'Tank 5','Tank 5',-6.948835,107.614982);";
    public static final String SQL_INSERT_FRIENDS_6 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (6,6,2, 1,1,'Tank 6','Tank 6',-6.948537,107.611849);";
    public static final String SQL_INSERT_FRIENDS_7 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (7,7,2, 1,1,'Tank 7','Tank 7',-6.948026,107.607644);";
    public static final String SQL_INSERT_FRIENDS_8 = "INSERT INTO 'friends' (friendid, address, parentid, categoryid, symbolid, title, descr, lat, lon) VALUES (8,8,2, 1,1,'Tank 8','Tank 8',-6.950496,107.624338);";

    public static final String SQL_DROP_chat = "DROP TABLE IF EXISTS 'chat'; ";
    public static final String SQL_DROP_roomchat = "DROP TABLE IF EXISTS 'roomchat'; ";
    public static final String SQL_DROP_manual_ops = "DROP TABLE IF EXISTS 'manual_ops'; ";
    public static final String SQL_DROP_friends = "DROP TABLE IF EXISTS 'friends'; ";
    public static final String SQL_DROP_foes = "DROP TABLE IF EXISTS 'foes'; ";
    public static final String SQL_DROP_friend_category = "DROP TABLE IF EXISTS 'friend_category'; ";
    public static final String SQL_DROP_friend_type = "DROP TABLE IF EXISTS 'friend_type'; ";
    public static final String SQL_DROP_friend_subtype = "DROP TABLE IF EXISTS 'friend_subtype'; ";
    public static final String SQL_ALTER_identity = "ALTER TABLE identity ADD COLUMN link_type VARCHAR DEFAULT 'ETH'; ";
    public static final String SQL_ALTER_identity2 = "ALTER TABLE identity ADD COLUMN subtypeid INTEGER DEFAULT '0'; ";

    public static final String SQL_DB = "DROP DATABASE IF EXIST 'geodata';";
    public static final String SQL_COUNT_POI = "SELECT COUNT(*) FROM points;";
    //New for FTP 281116
    public static final String SQL_COUNT_FTP = "SELECT COUNT(*) FROM ftp;";
    public static final String SQL_COUNT_SKN = "SELECT COUNT(*) FROM pskenario;";
    public static final String SQL_PointSrcID = "SELECT pointsourceid FROM points;";
    //
    public static final String SQL_FTPFileID = "SELECT fileid FROM ftp;";
    public static final String SQL_SkenaSrcID = "SELECT skenariosrcid FROM pskenario;";
    public static final String SQL_PointID = "SELECT pointid FROM points;";
    public static final String SQL_SkenaID = "SELECT pskenarioid,skenariosrcid FROM pskenario;";
    public static final String SQL_SearchSkenaSrcId = "SELECT pskenarioid,skenariosrcid FROM pskenario;";
    public static final String STAT_updateIdentity = "UPDATE identity SET subtype = @2 WHERE id = @1";
}
