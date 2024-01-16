package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") // localhost:8090/board/write
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, @RequestParam(name="file") MultipartFile file) throws Exception {

        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(value="searchKeyword", required=false) String searchKeyword) {

        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1; // Pageable은 0부터 시작하므로 1을 더해줌
        int startPage = Math.max(nowPage - 4, 1); // Math.max: 매개변수로 들어온 두 값을 비교해서 더 높은 값을 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // Math.min: 매개변수로 들어온 두 값을 비교해서 페이징보다 더 큰 값일 시 totalPage 반환

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8090/board/view?id=?
    public String boardView(Model model, @RequestParam(name="id") Integer id) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
        public String boardDelete(@RequestParam(name="id") Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) { /* PathVariable : 쿼리스트링이 아닌 /id 값으로 넘어감 */

        model.addAttribute("board", boardService.boardView(id));

        return "/boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, @RequestParam(name="file") MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id); // 기존 객체 불러와서 임시로 담아둠
        boardTemp.setTitle(board.getTitle()); // 기존 객체에 받은 값 저장
        boardTemp.setContent(board.getContent()); // 기존 객체에 받은 값 저장

        boardService.write(boardTemp, file); // 받은 값 저장한 객체 save

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }
}
