package com.project.eat.Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional//(readOnly = true) //해당 트랜잭션을 읽기 전용 다르게 사용할거면 false 값을 적용
public class RegisterService {

    @Autowired
    private RegisterDAO_JPA jpa;


    public RegisterVO_JPA insertOK(RegisterVO_JPA vo) {
        return jpa.save(vo); //pk 즉 num값이 있으면 수정, 없으면 입력, dao재정의 필요없음
    }
    //vo를 데이터베이스에 저장하고 저장된 결과를 반환합니다. pk(num) 값이 있으면 수정, 없으면 새로운 데이터를 입력합니다.
    public RegisterVO_JPA updateOK(RegisterVO_JPA vo) {
        return jpa.save(vo); //pk 즉 num값이 있으면 수정, 없으면 입력, dao재정의 필요없음
    }

    public int deleteOK(RegisterVO_JPA vo) {
        return jpa.deleteByNum(vo.getNum()); //함수커스텀
    }

    public List<RegisterVO_JPA> selectAll() {
//		return jpa.findAll();
//		return jpa.findByOrderByNumDesc();//역정렬해주는 메소드를 jpa규칙에 따라 빌드 가능
//		return jpa.findByOrderByNumAsc();//순정렬해주는 메소드를 jpa규칙에 따라 빌드 가능
        return jpa.selectAll_JPQL();//JPQL
    }
    public List<RegisterVO_JPA> selectAllPageBlock(int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;

        return jpa.selectAllPageBlock(startRow-1, pageBlock);//네이티브쿼리사용 함수
    }

    public RegisterVO_JPA selectOne(RegisterVO_JPA vo) {
        return jpa.findByNum(vo.getNum());
    }

    public List<RegisterVO_JPA> searchList(String searchKey, String searchWord) {

        //대소문자 구분함.
//		if(searchKey.equals("id")) {
//			return jpa.findByIdLike("%"+searchWord+"%");
//		}else if(searchKey.equals("name")) {
//			return jpa.findByNameLike("%"+searchWord+"%");
//		}else {
//			return jpa.findByTelLike("%"+searchWord+"%");
//		}

//		//대소문자 구분안함.
//		if(searchKey.equals("id")) {
//			return jpa.findByIdIgnoreCaseLike("%"+searchWord+"%");
//		}else if(searchKey.equals("name")) {
//			return jpa.findByNameIgnoreCaseLike("%"+searchWord+"%");
//		}else {
//			return jpa.findByTelIgnoreCaseLike("%"+searchWord+"%");
//		}
        //대소문자 구분안함.+ 정렬
        if(searchKey.equals("id")) {
            return jpa.findByIdIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
        }else if(searchKey.equals("name")) {
            return jpa.findByNameIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
        }else {
            return jpa.findByTelIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
        }
    }

    public List<RegisterVO_JPA> searchListPageBlock(
            String searchKey, String searchWord,
            int cpage, int pageBlock) {

        int startRow = (cpage - 1) * pageBlock + 1;

        if(searchKey.equals("id")) {
            return jpa.searchListID_PAGE("%"+searchWord+"%",startRow-1,pageBlock);//네이티브 쿼리
        }else {
            return jpa.searchListNAME_PAGE("%"+searchWord+"%",startRow-1,pageBlock);//네이티브 쿼리
        }
    }

    public long getTotalRows() {
        return jpa.count();//내장함수 : dao에 재정의 안해도됨.
    }

    public long getSearchTotalRows(String searchKey, String searchWord) {

        if(searchKey.equals("id")) {
            return jpa.count_id("%"+searchWord+"%");
        }else {
            return jpa.count_name("%"+searchWord+"%");
        }
    }

    public RegisterVO_JPA loginOK(RegisterVO_JPA vo) {
        return jpa.findByLogin(vo.getId(),vo.getPw());
    }

    public String getSalt(RegisterVO_JPA vo) {
        return jpa.findBySalt(vo.getId());
    }

}
