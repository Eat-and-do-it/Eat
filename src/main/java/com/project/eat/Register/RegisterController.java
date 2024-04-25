package com.project.eat.Register;

import com.project.eat.Register.RegisterService;
import com.project.eat.Register.RegisterVO_JPA;
import com.project.eat.Register.User_pwSHA512;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class RegisterController {

    @Autowired
    private RegisterService service;

    @Autowired
    private HttpSession session;


//    @GetMapping("/member/insert")
//    public String insert(Model model) {
//        log.info("/member/insert...");
//
//        return "member/insert";
//    }
//
//    @GetMapping("/member/login")
//    public String login(Model model) {
//        log.info("/member/login...");
//
//        return "member/login";
//    }
//
    @GetMapping("/member/logout")
    public String logout() {
        log.info("/member/logout...");

        session.removeAttribute("user_id");

        return "redirect:/";
    }


    @PostMapping("/member/loginOK")
    public String loginOK(RegisterVO_JPA vo) throws NoSuchAlgorithmException {
        log.info("/member/loginOK..."); // "/member/loginOK..." 메시지를 로그에 기록합니다.
        log.info("{}", vo); // vo 객체를 로그에 기록합니다.

        String salt = service.getSalt(vo);
        log.info("Salt : {}", salt);
        //k8C2IX+McOvgwDRYrnjeLw==

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(), salt);//복호화
        log.info("암호화 결과 : {}", hex_password);
        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
        vo.setPw(hex_password);

        RegisterVO_JPA vo2 = service.loginOK(vo); // vo를 사용하여 로그인을 시도합니다.
        log.info("{}", vo2); // 로그인 결과를 로그에 기록합니다.


        if (vo2 != null) { // 로그인 결과가 null이 아니면
            session.setAttribute("user_id", vo2.getId()); // 세션에 사용자 ID를 저장하고
            return "redirect:/"; // 홈 페이지로 리다이렉트합니다.
        } else { // 그렇지 않으면
            return "redirect:login"; // 로그인 페이지로 리다이렉트합니다.
        }

    }

    //==================================수정 기존:/member/insertOK... ==========================
    @PostMapping("/Register/registerOK")
    public String registerOK(RegisterVO_JPA vo) {
        log.info("/Register/registerOK..."); // "/member/insertOK..." 메시지를 로그에 기록합니다.
        log.info("vo:{}", vo); // vo 객체를 로그에 기록합니다.

        String salt = User_pwSHA512.Salt(); // Salt를 생성합니다.
        log.info("Salt : {}", salt);
        //k8C2IX+McOvgwDRYrnjeLw==
        vo.setSalt(salt);//디비에 저장-복호화 할때 사용

        String hex_password = User_pwSHA512.getSHA512(vo.getPw(), salt);//암호화
        log.info("암호화 결과 : {}", hex_password);
        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
        vo.setPw(hex_password);//디비에 저장

        RegisterVO_JPA result = service.insertOK(vo); // 회원 정보를 데이터베이스에 저장합니다.
        log.info("result:{}", result); // 저장 결과를 로그에 기록합니다.

        if (result != null) { // 저장 결과가 null이 아니면
            return "redirect:join"; // 모든 회원 정보를 조회하는 페이지로 리다이렉트합니다. /제거가 전부
        } else { // 그렇지 않으면
            return "redirect:/"; // 회원 등록 페이지로 리다이렉트합니다.
        }
    }
}



//    @GetMapping("/member/selectAll")
//    public String selectAll(@RequestParam(defaultValue = "1") int cpage,
//                            @RequestParam(defaultValue = "5") int pageBlock,Model model) {
//        log.info("/member/selectAll...");
//        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);
//
////		List<MemberVO_JPA> vos = service.selectAll();
//        List<RegisterVO_JPA> vos = service.selectAllPageBlock(cpage, pageBlock);
//
//        //MemberDAO_JPA에서 비밀번호가 너무길어서 10글자만 잘라서 반환함.
//        model.addAttribute("vos", vos);
//
//        // member테이블에 들어있는 모든회원수는 몇명?
//        long total_rows = service.getTotalRows();
//        log.info("total_rows:" + total_rows);
//
//        long totalPageCount = 1;
//        if (total_rows / pageBlock == 0) {
//            totalPageCount = 1;
//        } else if (total_rows % pageBlock == 0) {
//            totalPageCount = total_rows / pageBlock;
//        } else {
//            totalPageCount = total_rows / pageBlock + 1;
//        }
//        // 페이지 링크 몇개?
//        log.info("totalPageCount:" + totalPageCount);
//        model.addAttribute("totalPageCount", totalPageCount);
////		model.addAttribute("totalPageCount", 10);//테스트용
//
//        return "member/selectAll";
//    }

//    @GetMapping("/member/searchList")
//    public String searchList(@RequestParam(defaultValue = "1") int cpage,
//                             @RequestParam(defaultValue = "5") int pageBlock, String searchKey, String searchWord, Model model) {
//        log.info("/member/searchList...");
//        log.info("searchKey:{}", searchKey);
//        log.info("searchWord:{}", searchWord);
//        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);
//
////		List<MemberVO_JPA> vos = service.searchList(searchKey,searchWord);
//        List<RegisterVO_JPA> vos = service.searchListPageBlock(searchKey, searchWord, cpage, pageBlock);
//
//        model.addAttribute("vos", vos);
//
//
//        // 키워드검색 모든회원수는 몇명?
//        long total_rows = service.getSearchTotalRows(searchKey, searchWord);
//        log.info("total_rows:" + total_rows);
//
//        long totalPageCount = 1;
//        if (total_rows / pageBlock == 0) {
//            totalPageCount = 1;
//        } else if (total_rows % pageBlock == 0) {
//            totalPageCount = total_rows / pageBlock;
//        } else {
//            totalPageCount = total_rows / pageBlock + 1;
//        }
//        // 페이지 링크 몇개?
//        model.addAttribute("totalPageCount", totalPageCount);
////		model.addAttribute("totalPageCount", 10);//테스트용
//
//
//        return "member/selectAll";
//    }

//    @GetMapping("/member/selectOne")
//    public String selectOne(RegisterVO_JPA vo, Model model) {
//        log.info("/member/selectOne...");
//        log.info("vo:{}",vo);
//
//        RegisterVO_JPA vo2 = service.selectOne(vo);
//        vo2.setPw(vo2.getPw().substring(0,10));
//        model.addAttribute("vo2", vo2);
//
//        return "member/selectOne";
//    }

//    @PostMapping("/member/updateOK")
//    public String updateOK(RegisterVO_JPA vo) {
//        log.info("/member/updateOK...");
//        log.info("vo:{}",vo);
//
//        String salt = User_pwSHA512.Salt();
//        log.info("Salt : {}",salt);
//        //k8C2IX+McOvgwDRYrnjeLw==
//        vo.setSalt(salt);//디비에 저장-복호화 할때 사용
//
//        String hex_password = User_pwSHA512.getSHA512(vo.getPw(),salt);//암호화
//        log.info("암호화 결과 : {}", hex_password);
//        //c2ba573ac2595ebfac7f94c806b9e6279141057841f03b9b6f82e1cd114505eedabaf0cef9326cf470ff18941b4e780a4a5bf430e9a29bf1e538d37eece99289
//        vo.setPw(hex_password);//디비에 저장
//
//        //수정일자 반영안하면 null값이 들어가는 것을 방지하기위해...
//        if(vo.getRegdate()==null) {
//            vo.setRegdate(new Date());
//        }
//
//        RegisterVO_JPA result = service.updateOK(vo);
//        log.info("result:{}", result);
//
//        return "redirect:selectAll";
//
//    }

//    @GetMapping("/member/delete")
//    public String delete(Model model) {
//        log.info("/member/delete...");
//
//        return "member/delete";
//    }
//
//    // m_deleteOK 삭제시 반드시 @Transactional선언.
//    @Transactional
//    @PostMapping("/member/deleteOK")
//    public String deleteOK(RegisterVO_JPA vo) {
//        log.info("/member/deleteOK...");
//        log.info("vo:{}",vo);
//
//        int result = service.deleteOK(vo);
//        log.info("result:{}", result);
//
//        return "redirect:selectAll";
//    }
//
//}
