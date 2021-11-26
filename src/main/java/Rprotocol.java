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
    public static final int SUJECT_PLAN_RESULT = 40;//강의 계획서 결과
    public static final int MY_TIMETABLE_REQ = 41;//시간표 요청
    public static final int ACCOUNT_INFO_RESULT = 99;//로그인 결과
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
    //STUTYPE31
    //STUTYPE32
    //STUTYPE33
    //STUTYPE34
    //STUTYPE35
    //STUTYPE36
    //STUTYPE37
    //STUTYPE38
    //STUTYPE39
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
                        packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE+LEN_MAX];
                        break;
                    case ACCOUNT_INFO_REQ:
                        switch (protocolCode) {
                            case PT_UNDEFINED:
                            case STU_ID_PWD_REQ_CODE://id,pwd요청(서버)
                            case WHO_INFO_REQ://직책정보요청(서버)
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE+LEN_MAX];
                                break;
                        }
                        break;
                    case ACCOUNT_INFO_ANS:
                        switch (protocolCode) {
                            case PT_UNDEFINED:
                            case WHO_INFO_ANS://직책정보응답(클라)
                            case STU_ID_PWD_ANS_CODE://id,pwd 입력값(클라)
                                packet = new byte[LEN_PROTOCOL_TYPE + STU_LEN_PROTOCOL_TYPE + LEN_CODE+LEN_MAX];
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
                }
            } else {

                switch (stuprotocoltype) {//학생관련 프로토콜

                }
            }
        }
        packet[0] = (byte) protocolType;
        packet[1] = (byte) stuprotocoltype;
        packet[2] = (byte) protocolCode;
        return packet;
    }

    //write할 buf 바이트배열 타입,코드에맞게 생성한 packet으로 복사해줌
    public void setPacket(int pt, int spt, int code, byte[] buf) {
        packet = null;
        packet = getPacket(pt, spt, code);
        System.arraycopy(buf, 0, packet, 3, buf.length);
        packet[3+buf.length-1]='\0';
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
    public void setPacket_type_and_code(int type,int stutype,int code){
        packet[0]=(byte)type; packet[1]=(byte)stutype; packet[2]=(byte)code;

    }

}