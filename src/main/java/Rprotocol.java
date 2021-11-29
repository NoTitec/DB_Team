import java.nio.ByteBuffer;

public class Rprotocol {
    //포맷정보
    public static final int PT_UNDEFINED = -1;
    public static final int PT_EXIT = 0;
    public static final int LEN_PROTOCOL_TYPE = 1;//프로토콜 타입 길이
    public static final int STU_LEN_PROTOCOL_TYPE = 1;//학생 프로토콜 타입 길이
    public static final int LEN_CODE = 1;//프로토콜 코드 길이

    public static final int LEN_SUBJECT_COUNT = 4;//교과목 개수 길이
    public static final int LEN_MAX = 100000;//최대 데이터 길이
    //---------------------------------------------------------------------------------
    //TYPE정보
    public static final int ACCOUNT_INFO_REQ = 1;//계정 정보 요청
    public static final int ACCOUNT_INFO_ANS = 2;//계정 정보 응답

    public static final int REGIST_REQ = 30;//수강 요청
    public static final int CREATE_SUBJECT_INFO_ANS = 31;//개설 교과목 정보 응답
    public static final int SEL_SUBJECT_ANS = 32;//선택 교과목 응답
    public static final int MY_REGIST_ANS = 33;//본인 수강목록 응답
    public static final int REGIST_RESULT = 34;//수강 요청 결과
    public static final int REGIST_CANSEL_RESULT = 35;//수강 취소 결과
    public static final int STU_INFO_UPDATE_REQ = 36;//학생 정보 수정 요청
    public static final int STU_INFO_UPDATE_ANS = 37;//학생 정보 수정 응답
    public static final int STU_INFO_UPDATE_RESULT = 38;//학생 정보 수정 결과
    public static final int CREATE_SUBJECT_INFO_REQ = 39;//개설 교과목 정보 요청
    public static final int SUBJECT_PLAN_RESULT = 40;//강의 계획서 결과
    public static final int MY_TIMETABLE_REQ = 41;//시간표 요청
    public static final int ACCOUNT_INFO_RESULT = 99;//로그인 결과
    public static final int MENU_REQ=100;//메뉴요청
    //------------------------------------------------------------------------------------
    //CODE정보
    //TYPE01
    public static final int STU_ID_PWD_REQ_CODE = 9;//학생 ID,PW요청
    public static final int WHO_INFO_REQ = 11;
    //TYPE02
    public static final int STU_ID_PWD_ANS_CODE = 9;//학생id,pwd응답
    public static final int WHO_INFO_ANS = 11;
    //TYPE99
    public static final int STU_LOGIN_SUCCESS_CODE = 3;//학생로그인 성공
    public static final int STU_LOGIN_FAIL_CODE = 4;//학생로그인 실패

    //STUTYPE30
    public static final int REGIST_REQ_CODE = 1;
    public static final int REGIST_CANCEL_CODE = 2;
    //STUTYPE31
    public static final int CREAT_SUBJECT_GRADE_CODE = 1;
    public static final int CREAT_SUBJECT_GRADE_ALL_CODE = 2;
    public static final int CREAT_SUBJECT_GRADE_ALL_AND_PLAN_CODE = 3;
    //STUTYPE32
    public static final int SELECT_REGIST_SUBJECT_CODE = 1;
    public static final int SELECT_CANCLE_SUBJECT_CODE = 2;
    public static final int SELECT_PLAN_SUBJECT_CODE = 3;
    //STUTYPE33
    public static final int SUBJECT_CODE_INFO_CODE = 1;
    public static final int SUBJECT_TIME_INFO_CODE = 2;
    //STUTYPE34
    public static final int REGIST_SUCESS_CODE = 1;
    public static final int NOT_REGIST_DAY_CODE = 2;
    public static final int DUP_TIME_CODE = 3;
    public static final int MAX_STU_CODE = 4;
    //STUTYPE35
    //STUTYPE36
    public static final int PWD_UPDATE_REQ_CODE = 1;
    public static final int PWD_UPDATE_IN_REQ_CODE = 2;
    //STUTYPE37
    //STUTYPE38
    //STUTYPE39
    public static final int SIMPLE_LOOK_CODE = 1;
    public static final int SUBJECT_PLAN_REQ_CODE = 2;
    //STUTYPE40
    //STUTYPE41

    protected int protocolType;
    protected int stuprotocoltype;
    protected int protocolCode;
    private byte[] packet;

    public Rprotocol() {
        this(PT_UNDEFINED, PT_UNDEFINED, PT_UNDEFINED);
    }

    public Rprotocol(int protocolType, int stuprotocoltype, int protocolCode) {
        this.protocolType = protocolType;
        this.stuprotocoltype = stuprotocoltype;
        this.protocolCode = protocolCode;

        getPacket(protocolType, stuprotocoltype, protocolCode);
    }

    public byte[] getPacket() {
        return packet;
    }

    //프로토콜 타입,코드따라서 길이 다르게 생성
    public byte[] getPacket(int protocolType, int stuprotocoltype, int protocolCode) {//사용안하는필드는 -1으로넣음
        if (packet == null) {
            if (stuprotocoltype == PT_UNDEFINED) {//학생관련프로토콜값이 -1이면 다른 타입
                switch (protocolType) {
                    case PT_UNDEFINED:
                        packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                        break;
                    case ACCOUNT_INFO_REQ:
                        switch (protocolCode) {
                            case PT_UNDEFINED:
                            case STU_ID_PWD_REQ_CODE://id,pwd요청(서버)
                            case WHO_INFO_REQ://직책정보요청(서버)
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                                break;
                        }
                        break;
                    case ACCOUNT_INFO_ANS:
                        switch (protocolCode) {
                            case PT_UNDEFINED:
                            case WHO_INFO_ANS://직책정보응답(클라)
                            case STU_ID_PWD_ANS_CODE://id,pwd 입력값(클라)
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                                break;
                        }
                        break;
                    case ACCOUNT_INFO_RESULT:
                        switch (protocolCode) {
                            case PT_UNDEFINED:
                            case STU_LOGIN_SUCCESS_CODE://로그인성공결과(서버)
                            case STU_LOGIN_FAIL_CODE://로그인실패결과(서버)
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                                break;
                        }
                        break;
                    case MENU_REQ:
                        packet= new byte[LEN_PROTOCOL_TYPE+ STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                }
            } else {//학생관련 프로토콜

                switch (stuprotocoltype) {
                    case REGIST_REQ:
                        switch (protocolCode) {
                            case Rprotocol.REGIST_REQ_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                                break;
                            case Rprotocol.REGIST_CANCEL_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                        }

                        break;
                    case CREATE_SUBJECT_INFO_REQ:
                        switch (protocolCode){
                            case SIMPLE_LOOK_CODE:
                            case SUBJECT_PLAN_REQ_CODE:
                                packet=new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                            break;
                        }
                        break;
                    case CREATE_SUBJECT_INFO_ANS:
                        switch (protocolCode) {
                            case Rprotocol.CREAT_SUBJECT_GRADE_CODE:
                            case Rprotocol.CREAT_SUBJECT_GRADE_ALL_CODE:
                            case Rprotocol.CREAT_SUBJECT_GRADE_ALL_AND_PLAN_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                                break;
                        }
                        break;
                    case SEL_SUBJECT_ANS:
                        switch (protocolCode) {
                            case SELECT_REGIST_SUBJECT_CODE:
                            case SELECT_CANCLE_SUBJECT_CODE:
                            case SELECT_PLAN_SUBJECT_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + 50];
                                break;
                        }
                        break;
                    case REGIST_RESULT:
                        switch (protocolCode) {
                            case REGIST_SUCESS_CODE:
                            case NOT_REGIST_DAY_CODE:
                            case DUP_TIME_CODE:
                            case MAX_STU_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                                break;
                        }
                        break;
                    case MY_REGIST_ANS:
                        switch (protocolCode){
                            case SUBJECT_CODE_INFO_CODE:
                            case SUBJECT_TIME_INFO_CODE:
                                packet= new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                                break;
                        }
                        break;
                    case REGIST_CANSEL_RESULT:
                    case STU_INFO_UPDATE_RESULT:
                    case MY_TIMETABLE_REQ:
                        packet= new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                        break;
                    case STU_INFO_UPDATE_REQ:
                        switch (protocolCode) {
                            case PWD_UPDATE_REQ_CODE:
                            case PWD_UPDATE_IN_REQ_CODE:
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE];
                                break;
                        }
                        break;
                    case STU_INFO_UPDATE_ANS:
                        packet=new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE+70];
                        break;
                    case SUBJECT_PLAN_RESULT:
                        packet= new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE + LEN_MAX];
                        break;
                }
            }
        }
        packet[0] = (byte) protocolType;
        packet[1] = (byte) stuprotocoltype;
        packet[2] = (byte) protocolCode;
        return packet;
    }

    //write할 buf 바이트배열 타입,코드에맞게 생성한 packet으로 복사해줌
    public void setPacket(int pt, int spt, int code) {//data없는경우
        packet = null;
        packet = getPacket(pt, spt, code);
    }

    public void setPacket(int pt, int spt, int code, byte[] buf) {//data있는경우
        packet = null;
        packet = getPacket(pt, spt, code);
        System.arraycopy(buf, 0, packet, 3, buf.length);
        packet[3 + buf.length - 1] = '\0';
    }

    public byte[] intto4byte(int intvalue) {//int값을 4byte byte배열로 전환후 반환
        byte[] byteArray = ByteBuffer.allocate(4).putInt(intvalue).array();

        return byteArray;
    }

    public int byteArrayToInt(byte bytes[]) {//4byte 배열을 int 값으로 전환해주는 메소드
        return ((((int) bytes[0] & 0xff) << 24) |
                (((int) bytes[1] & 0xff) << 16) |
                (((int) bytes[2] & 0xff) << 8) |
                (((int) bytes[3] & 0xff)));
    }

    public byte[] IntToBytes(int data) {
        return new byte[]{
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }

    public void setPacket_type_and_code(int type, int stutype, int code) {
        packet[0] = (byte) type;
        packet[1] = (byte) stutype;
        packet[2] = (byte) code;

    }

}