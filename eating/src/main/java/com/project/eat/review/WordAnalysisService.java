package com.project.eat.review;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Slf4j
@Service("WordAnalysisService")
public class WordAnalysisService implements IWordAnalysisService {

    @Autowired
    private BadWordService badWordService;

    //자연어 처리 - 형태소 분석기인 Komoran를 메모리에 올리기 위해 WordAnalysisService 클래스 내 전역 변수로 설정합니다.
    Komoran nlp = null;

    //생성자 사용함 - 톰켓에서 부팅할 때 @Service를 모두 메모리에 올립니다.
    //톰켓이 메모리에 올릴 때, 생성자에 선언한 Komoran도 같이 메모리에 올라가도록 생성자에 코딩합니다.
    //생성자에서 Komoran을 메모리에 올리면, 매번 메모리에 올려서 호출하는 것이 아니라,
    // 메모리에 올리간 객체만 불러와서 사용할 수 있기 때문에 처리 속도가 빠릅니다.
    public WordAnalysisService() {

        log.info(this.getClass().getName() + ".WordAnalysisService creator Start !");

        //NLP 분석 객체 메모리 로딩합니다.
        this.nlp = new Komoran(DEFAULT_MODEL.LIGHT); // 학습데이터 경량화 버전( 웹 서비스에 적합합니다. )
        //this.nlp = new Komoran(DEFAULT_MODEL.FULL); // 학습데이터 전체 버전(일괄처리 : 배치 서비스에 적합합니다.)

        log.info("난 톰켓이 부팅되면서 스프링 프렝미워크가 자동 실행되었고, 스프링 실행될 때 nlp 변수에 Komoran 객체를 생성하여 저장하였다.");

        log.info(this.getClass().getName() + ".WordAnalysisService creator End !");


    }

    @Override
    public List<String> doWordNouns(String text) throws Exception {

        log.info(this.getClass().getName() + ".doWordAnalysis Start !");

        log.info("분석할 문장 : " + text);

        //분석할 문장에 대해 정제(쓸데없는 특수문자 제거)
        String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");

        log.info("한국어, 영어, 숫자 제외 단어 모두 한 칸으로 변환시킨 문장 : " + replace_text);

        //분석할 문장의 앞, 뒤에 존재할 수 있는 필요없는 공백 제거
        String trim_text = replace_text.trim();

        log.info("분석할 문장 앞, 뒤에 존재할 수 있는 필요 없는 공백 제거 : " + trim_text);

        //형태소 분석 시작
        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);

        //형태소 분석 결과 중 명삼나 가져오기
        List<String> rList = analyzeResultList.getNouns();

        if (rList == null) {
            rList = new ArrayList<String>();
        }
        log.info("rList 확인.. rList:{}",rList);

        List<BadwordVO> badWordList = badWordService.badwordVOList();
        log.info("db에서 비속어 잘 가져오나 확인! badWordList:{}",badWordList);
        // 독립된 단어로 나쁜말찾기
        String[] txt = trim_text.split(" " );
        String[] vo = new String[badWordList.size()];
        for(int i=0; i<vo.length;i++){
            vo[i]=badWordList.get(i).getBadWord();
            for(String x: txt){
                if(x.equals(vo[i])){
                    log.info("나쁜말존재 x:{}",x);
                }
            }
        }

        // 명사 나쁜말찾기
//        List<String> badList = new ArrayList<>();
//        for(int i=0; i<=badWordList.size(); i++){
//            badList.add(badWordList.get(i).getBadWord());
//        }
//        Set<String> set = new HashSet<>(badList);
//        boolean hasDuplicates = false;
//        for (String element : rList) {
//            if (set.contains(element)) {
//                hasDuplicates = true;
//                break;
//            }
//        }
//
//        if (hasDuplicates) {
//            System.out.println("두 리스트에 중복된 원소가 존재합니다 나쁜말 존재!. hasDuplicates:{},"+hasDuplicates);
//        } else {
//            System.out.println("두 리스트에 중복된 원소가 존재하지 않습니다.좋은말만있다");
//        }


        log.info(this.getClass().getName() + ".doWordAnalysis End !");

        return rList;
    }

    @Override
    public Map<String, Integer> doWordCount(List<String> pList) throws Exception {

        log.info(this.getClass().getName() + ".doWordCount Start !");

        if (pList ==null) {
            pList = new ArrayList<String>();
        }

        //단어 빈도수(사과, 3) 결과를 저장하기 위해 Map객체 생성합니다.
        Map<String, Integer> rMap = new HashMap<>();

        //List에 존재하는 중복되는 단어들의 중복제거를 위해 set 데이터타입에 데이터를 저장합니다.
        //rSet 변수는 중복된 데이터가 저장되지 않기 떄문에 중복되지 않은 단어만 저장하고 나머지는 자동 삭제합니다.
        Set<String> rSet = new HashSet<String>(pList);

        //중복이 제거된 단어 모음에 빈도수를 구하기 위해 반복문을 사용합니다.
        Iterator<String> it = rSet.iterator();

        log.info(this.getClass().getName() + ".doWordCount End !");

        return rMap;
    }

    @Override
    public Map<String, Integer> doWordAnalysis(String text) throws Exception {


        //문장의 명사를 추출하기 위한 형태소 분석 실행
        List<String> rList = this.doWordNouns(text);

        if(rList == null) {
            rList = new ArrayList<String>();
        }

        //추출된 명사 모음(리스트)의 명사 단어별 빈도수 계산
        Map<String, Integer> rMap = this.doWordCount(rList);

        if(rMap == null) {
            rMap = new HashMap<String, Integer>();
        }

        return rMap;
    }

    @Override
    public int checkdoBadWord(String text) throws Exception {
        int flag = 1;

        log.info(this.getClass().getName() + ".doWordAnalysis Start !");

        log.info("분석할 문장 : " + text);

        //분석할 문장에 대해 정제(쓸데없는 특수문자 제거)
        String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");

        log.info("한국어, 영어, 숫자 제외 단어 모두 한 칸으로 변환시킨 문장 : " + replace_text);

        //분석할 문장의 앞, 뒤에 존재할 수 있는 필요없는 공백 제거
        String trim_text = replace_text.trim();

        log.info("분석할 문장 앞, 뒤에 존재할 수 있는 필요 없는 공백 제거 : " + trim_text);

        //형태소 분석 시작
        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);

        //형태소 분석 결과 중 명삼나 가져오기
        List<String> rList = analyzeResultList.getNouns();

        if (rList == null) {
            rList = new ArrayList<String>();
        }
        log.info("rList 확인.. rList:{}",rList);

        List<BadwordVO> badWordList = badWordService.badwordVOList();
        log.info("db에서 비속어 잘 가져오나 확인! badWordList:{}",badWordList);
        // 독립된 단어로 나쁜말찾기
        String[] txt = trim_text.split(" " );
        String[] vo = new String[badWordList.size()];
        for(int i=0; i<vo.length;i++){
            vo[i]=badWordList.get(i).getBadWord();
            for(String x: txt){
                if(x.equals(vo[i])){
                    log.info("나쁜말존재 x:{}",x);
                    flag = 0;
                    return flag;
                }
            }
        }

        // 명사 나쁜말찾기
        Iterator<String> it = rList.iterator();
        while(it.hasNext()) {
            String str = it.next();
            for(String a: vo){
                if(a.equals(str)){
                    log.info("rList 로 확인 !! 나쁜말존재 a:{}",a);
                    flag = 0;
                    return flag;
                }
            }

        }




//        List<String> badList = new ArrayList<>();
//        for(int i=0; i<=badWordList.size(); i++){
//            badList.add(badWordList.get(i).getBadWord());
//        }
//        Set<String> set = new HashSet<>(badList);
//        boolean hasDuplicates = false;
//        for (String element : rList) {
//            if (set.contains(element)) {
//                hasDuplicates = true;
//                flag =0;
//                break;
//            }
//        }
//
//        if (hasDuplicates) {
//            System.out.println("두 리스트에 중복된 원소가 존재합니다 나쁜말 존재!. hasDuplicates:{},"+hasDuplicates);
//        } else {
//            System.out.println("두 리스트에 중복된 원소가 존재하지 않습니다.좋은말만있다");
//        }



        return flag;
    }
}