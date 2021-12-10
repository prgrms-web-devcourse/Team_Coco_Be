package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreateRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.schedule.dto.request.Position;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;
import com.cocodan.triplan.schedule.dto.response.DailyScheduleSpotResponse;
import com.cocodan.triplan.schedule.service.ScheduleService;
import com.cocodan.triplan.spot.domain.vo.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchedulePostServiceTest {

    @Autowired
    SchedulePostService schedulePostService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    private static Long testMemberId;
    private static final String EMAIL = "KimLeePark@gmail.com";
    private static final String NAME = "김이박";
    private static final String PHONE = "01077775555";
    private static final String BIRTH = "19901111";
    private static final String GENDER = GenderType.MALE.getTypeStr();
    private static final String NICKNAME = "TestNickname";
    private static final String PROFILE_IMAGE = "https://wwww.someonesownserver.org/img/1";

    @BeforeAll
    void setup() {
        testMemberId = memberService.create(
                EMAIL,
                NAME,
                PHONE,
                BIRTH,
                GENDER,
                NICKNAME,
                PROFILE_IMAGE
        ).getId();
    }

    @Test
    @DisplayName("생성된 여행 공유 게시글 리스트를 정상적으로 조회 할 수 있다.")
    @Transactional
    void getRecentSchedulePostList() {
        // 여행 생성
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);

        // 여행 공유 게시글 생성
        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행 게시글")
                .content("1번 여행 게시글 본문")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);

        // 여행 공유 게시글 조회
        List<SchedulePostResponse> posts = schedulePostService.getSchedulePosts("", City.ALL, Theme.ALL, SchedulePostSortingRule.RECENT, 0);
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getProfileImageUrl()).isEqualTo(PROFILE_IMAGE);
        assertThat(posts.get(0).getNickname()).isEqualTo(NICKNAME);
        assertThat(posts.get(0).getTitle()).isEqualTo("1번 여행 게시글");
        assertThat(posts.get(0).getGenderType().getTypeStr()).isEqualTo(GENDER);
        assertThat(posts.get(0).getCity()).isEqualTo(City.SEOUL);
        assertThat(posts.get(0).getStartDate()).isEqualTo(LocalDate.of(2021, 12, 1));
        assertThat(posts.get(0).getEndDate()).isEqualTo(LocalDate.of(2021, 12, 3));
        assertThat(posts.get(0).getThema()).contains(Theme.ACTIVITY, Theme.FOOD);

        // TODO: 다양한 조건으로 테스트 추가
    }

    private ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest("title", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreationRequest(1L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 1),
                        new DailyScheduleSpotCreationRequest(2L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 2),
                        new DailyScheduleSpotCreationRequest(3L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 3),
                        new DailyScheduleSpotCreationRequest(4L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 1),
                        new DailyScheduleSpotCreationRequest(5L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 2),
                        new DailyScheduleSpotCreationRequest(6L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 3),
                        new DailyScheduleSpotCreationRequest(7L, "address7", "roadAddress7", "010-1111-2228", "불국사7", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 1),
                        new DailyScheduleSpotCreationRequest(8L, "address8", "roadAddress8", "010-1111-2229", "불국사8", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 2)
                ));
    }

    @Test
    @DisplayName("여행 공유 게시글 생성 로직이 정상적으로 동작한다.")
    @Transactional
    void createSchedulePost() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);

        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행!")
                .content("어디로든 갔다옴~")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);
        SchedulePost post1 = schedulePostService.findById(createdSchedulePostId);

        assertThat(post1.getMember().getId()).isEqualTo(testMemberId);
        assertThat(memberService.getOne(post1.getMember().getId()).getEmail()).isEqualTo(EMAIL);
        assertThat(memberService.getOne(post1.getMember().getId()).getName()).isEqualTo(NAME);
        assertThat(memberService.getOne(post1.getMember().getId()).getPhoneNumber()).isEqualTo(PHONE);
        assertThat(memberService.getOne(post1.getMember().getId()).getBirth()).isEqualTo(BIRTH);
        assertThat(memberService.getOne(post1.getMember().getId()).getNickname()).isEqualTo(NICKNAME);
        assertThat(memberService.getOne(post1.getMember().getId()).getProfileImage()).isEqualTo(PROFILE_IMAGE);
        assertThat(post1.getTitle()).isEqualTo("1번 여행!");
        assertThat(post1.getContent()).isEqualTo("어디로든 갔다옴~");
        assertThat(post1.getCity()).isEqualTo(City.SEOUL);
        assertThat(post1.getSchedule().getId()).isEqualTo(createdScheduleId);
    }

    @Test
    @DisplayName("여행 공유 게시글을 상세 조회 할 수 있다.")
    @Transactional
    void getSchedulePostDetail() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);
        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행!")
                .content("Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. Apple is the largest information technology company by revenue (totaling $274.5 billion in 2020) and, since January 2021, the world's most valuable company. As of 2021, Apple is the fourth-largest PC vendor by unit sales[9] and fourth-largest smartphone manufacturer.[10][11] It is one of the Big Five American information technology companies, alongside Amazon, Google (Alphabet), Facebook (Meta), and Microsoft.[12][13][14]\n" +
                        "\n" +
                        "Apple was founded in 1976 by Steve Jobs, Steve Wozniak and Ronald Wayne to develop and sell Wozniak's Apple I personal computer. It was incorporated by Jobs and Wozniak as Apple Computer, Inc. in 1977, and sales of its computers, among them the Apple II, grew quickly. It went public in 1980, to instant financial success. Over the next few years, Apple shipped new computers featuring innovative graphical user interfaces, such as the original Macintosh, announced in a critically acclaimed advertisement, \"1984\", directed by Ridley Scott. The high cost of its products and limited application library caused problems, as did power struggles between executives. In 1985, Wozniak departed Apple amicably,[15] while Jobs resigned to found NeXT, taking some Apple employees with him.[16]\n" +
                        "\n" +
                        "As the market for personal computers expanded and evolved throughout the 1990s, Apple lost considerable market share to the lower-priced duopoly of Microsoft Windows on Intel PC clones. The board recruited CEO Gil Amelio, who prepared the struggling company for eventual success with extensive reforms, product focus and layoffs in his 500-day tenure. In 1997, Amelio bought NeXT to resolve Apple's unsuccessful operating-system strategy and entice Jobs back to the company; he replaced Amelio. Apple became profitable again through a number of tactics. First, a revitalizing campaign called \"Think different\", and by launching the iMac and iPod. In 2001, it opened a retail chain, the Apple Stores, and has acquired numerous companies to broaden its software portfolio. In 2007, the company launched the iPhone to critical acclaim and financial success. Jobs resigned in 2011 for health reasons, and died two months later. He was succeeded as CEO by Tim Cook.\n" +
                        "\n" +
                        "The company receives significant criticism regarding the labor practices of its contractors, its environmental practices, and its business ethics, including anti-competitive behavior and materials sourcing. In August 2018, Apple became the first publicly traded U.S. company to be valued at over $1 trillion,[17][18] and, two years later, the first valued at over $2 trillion.[19][20] The company enjoys a high level of brand loyalty, and is ranked as the world's most valuable brand; as of January 2021, there are 1.65 billion Apple products in active use.[21]")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);
        SchedulePost post = schedulePostService.findById(createdSchedulePostId);

        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(createdSchedulePostId);

        assertThat(schedulePostDetail.getTitle()).isEqualTo("1번 여행!");
        assertThat(schedulePostDetail.getContent()).isEqualTo("Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. Apple is the largest information technology company by revenue (totaling $274.5 billion in 2020) and, since January 2021, the world's most valuable company. As of 2021, Apple is the fourth-largest PC vendor by unit sales[9] and fourth-largest smartphone manufacturer.[10][11] It is one of the Big Five American information technology companies, alongside Amazon, Google (Alphabet), Facebook (Meta), and Microsoft.[12][13][14]\n" +
                "\n" +
                "Apple was founded in 1976 by Steve Jobs, Steve Wozniak and Ronald Wayne to develop and sell Wozniak's Apple I personal computer. It was incorporated by Jobs and Wozniak as Apple Computer, Inc. in 1977, and sales of its computers, among them the Apple II, grew quickly. It went public in 1980, to instant financial success. Over the next few years, Apple shipped new computers featuring innovative graphical user interfaces, such as the original Macintosh, announced in a critically acclaimed advertisement, \"1984\", directed by Ridley Scott. The high cost of its products and limited application library caused problems, as did power struggles between executives. In 1985, Wozniak departed Apple amicably,[15] while Jobs resigned to found NeXT, taking some Apple employees with him.[16]\n" +
                "\n" +
                "As the market for personal computers expanded and evolved throughout the 1990s, Apple lost considerable market share to the lower-priced duopoly of Microsoft Windows on Intel PC clones. The board recruited CEO Gil Amelio, who prepared the struggling company for eventual success with extensive reforms, product focus and layoffs in his 500-day tenure. In 1997, Amelio bought NeXT to resolve Apple's unsuccessful operating-system strategy and entice Jobs back to the company; he replaced Amelio. Apple became profitable again through a number of tactics. First, a revitalizing campaign called \"Think different\", and by launching the iMac and iPod. In 2001, it opened a retail chain, the Apple Stores, and has acquired numerous companies to broaden its software portfolio. In 2007, the company launched the iPhone to critical acclaim and financial success. Jobs resigned in 2011 for health reasons, and died two months later. He was succeeded as CEO by Tim Cook.\n" +
                "\n" +
                "The company receives significant criticism regarding the labor practices of its contractors, its environmental practices, and its business ethics, including anti-competitive behavior and materials sourcing. In August 2018, Apple became the first publicly traded U.S. company to be valued at over $1 trillion,[17][18] and, two years later, the first valued at over $2 trillion.[19][20] The company enjoys a high level of brand loyalty, and is ranked as the world's most valuable brand; as of January 2021, there are 1.65 billion Apple products in active use.[21]");
        assertThat(schedulePostDetail.getCity()).isEqualTo(City.SEOUL);
        assertThat(schedulePostDetail.getCreatedAt()).isEqualTo(post.getCreatedDate());
        assertThat(schedulePostDetail.getViews()).isEqualTo(post.getViews());
        assertThat(schedulePostDetail.getLiked()).isEqualTo(post.getLiked());
        assertThat(schedulePostDetail.getStartDate()).isEqualTo(post.getSchedule().getStartDate());
        assertThat(schedulePostDetail.getEndDate()).isEqualTo(post.getSchedule().getEndDate());
        assertThat(schedulePostDetail.getGender()).isEqualTo(post.getMember().getGender());
        assertThat(schedulePostDetail.getNickname()).isEqualTo(post.getMember().getNickname());
        assertThat(schedulePostDetail.getAges()).isEqualTo(Ages.from(post.getMember().getBirth()));

        // TODO: 2021.12.10 Teru - equals method를 오버라이드 하지 않고, 그냥 DTO의 toString() 을 통해서 String 비교로 검증하도록 수정.
        Assertions.assertArrayEquals(
                schedulePostDetail.getDailyScheduleSpots().toArray(),
                post.getSchedule().getDailyScheduleSpots().stream()
                        .map(DailyScheduleSpotResponse::from)
                        .toArray()
                );
    }

    @Test
    @DisplayName("생성된 공유 게시글을 삭제할 수 있다")
    @Transactional
    void deleteSchedulePost() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);
        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행!")
                .content("Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software and online services. Apple is the largest information technology company by revenue (totaling $274.5 billion in 2020) and, since January 2021, the world's most valuable company. As of 2021, Apple is the fourth-largest PC vendor by unit sales[9] and fourth-largest smartphone manufacturer.[10][11] It is one of the Big Five American information technology companies, alongside Amazon, Google (Alphabet), Facebook (Meta), and Microsoft.[12][13][14]\n" +
                        "\n" +
                        "Apple was founded in 1976 by Steve Jobs, Steve Wozniak and Ronald Wayne to develop and sell Wozniak's Apple I personal computer. It was incorporated by Jobs and Wozniak as Apple Computer, Inc. in 1977, and sales of its computers, among them the Apple II, grew quickly. It went public in 1980, to instant financial success. Over the next few years, Apple shipped new computers featuring innovative graphical user interfaces, such as the original Macintosh, announced in a critically acclaimed advertisement, \"1984\", directed by Ridley Scott. The high cost of its products and limited application library caused problems, as did power struggles between executives. In 1985, Wozniak departed Apple amicably,[15] while Jobs resigned to found NeXT, taking some Apple employees with him.[16]\n" +
                        "\n" +
                        "As the market for personal computers expanded and evolved throughout the 1990s, Apple lost considerable market share to the lower-priced duopoly of Microsoft Windows on Intel PC clones. The board recruited CEO Gil Amelio, who prepared the struggling company for eventual success with extensive reforms, product focus and layoffs in his 500-day tenure. In 1997, Amelio bought NeXT to resolve Apple's unsuccessful operating-system strategy and entice Jobs back to the company; he replaced Amelio. Apple became profitable again through a number of tactics. First, a revitalizing campaign called \"Think different\", and by launching the iMac and iPod. In 2001, it opened a retail chain, the Apple Stores, and has acquired numerous companies to broaden its software portfolio. In 2007, the company launched the iPhone to critical acclaim and financial success. Jobs resigned in 2011 for health reasons, and died two months later. He was succeeded as CEO by Tim Cook.\n" +
                        "\n" +
                        "The company receives significant criticism regarding the labor practices of its contractors, its environmental practices, and its business ethics, including anti-competitive behavior and materials sourcing. In August 2018, Apple became the first publicly traded U.S. company to be valued at over $1 trillion,[17][18] and, two years later, the first valued at over $2 trillion.[19][20] The company enjoys a high level of brand loyalty, and is ranked as the world's most valuable brand; as of January 2021, there are 1.65 billion Apple products in active use.[21]")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);
        SchedulePost post = schedulePostService.findById(createdSchedulePostId);

        // 게시글 생성 확인
        assertThat(schedulePostService.findById(createdSchedulePostId).getId()).isEqualTo(createdScheduleId);

        // 게시글 제거 가능여부 검증
        Assertions.assertThrows(RuntimeException.class,
                () -> schedulePostService.validateRemovable(-999L, post.getId())
        );

        // 게시글 삭제하기
        schedulePostService.validateRemovable(testMemberId, createdSchedulePostId);
        schedulePostService.deleteSchedulePost(createdScheduleId);

        // 게시글이 삭제되었는지 검증하기
        Assertions.assertThrows(RuntimeException.class,
                () -> schedulePostService.findById(createdSchedulePostId)
        );
    }
}