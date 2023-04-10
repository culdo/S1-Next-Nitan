package me.ykrank.s1next.data.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import me.ykrank.s1next.data.api.model.Profile;
import me.ykrank.s1next.data.api.model.collection.Favourites;
import me.ykrank.s1next.data.api.model.collection.Friends;
import me.ykrank.s1next.data.api.model.collection.Notes;
import me.ykrank.s1next.data.api.model.collection.PmGroups;
import me.ykrank.s1next.data.api.model.darkroom.DarkRoomWrapper;
import me.ykrank.s1next.data.api.model.wrapper.AccountResultWrapper;
import me.ykrank.s1next.data.api.model.wrapper.BaseDataWrapper;
import me.ykrank.s1next.data.api.model.wrapper.BaseResultWrapper;
import me.ykrank.s1next.data.api.model.wrapper.PmsWrapper;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface S1Service {

    @GET(ApiForum.URL_FORUM)
    Single<String> getForumGroupsWrapper();

    @GET(ApiHome.URL_FAVOURITES)
    Single<BaseResultWrapper<Favourites>> getFavouritesWrapper(@Query("page") int page);

    @GET(ApiForum.URL_THREAD_LIST)
    Single<String> getThreadsWrapper(@Query("fid") String forumId, @Query("typeid") String typeId, @Query("page") int page);

    @GET(ApiForum.URL_POST_LIST)
    Single<String> getPostsWrapper(@Query("tid") String threadId, @Query("page") int page, @Query("authorid") String authorId);

    @GET(ApiForum.URL_POST_LIST_NEW)
    Single<String> getPostsWrapperNew(@Query("tid") String threadId, @Query("page") int page, @Query("authorid") String authorId);

    @GET(ApiForum.URL_TRADE_POST_INFO)
    Single<String> getTradePostInfo(@Query("tid") String threadId, @Query("pid") int pid);

    @GET(ApiForum.URL_QUOTE_POST_REDIRECT)
    Single<Response<Void>> getQuotePostResponseBody(@Query("ptid") String threadId, @Query("pid") String quotePostId);

    @FormUrlEncoded
    @POST(ApiMember.URL_LOGIN)
    Single<AccountResultWrapper> login(@Field("username") String username, @Field("password") String password);

    @GET(ApiForum.URL_AUTHENTICITY_TOKEN_HELPER)
    Single<AccountResultWrapper> refreshAuthenticityToken();

    //region Favourites
    @FormUrlEncoded
    @POST(ApiHome.URL_THREAD_FAVOURITES_ADD)
    Single<AccountResultWrapper> addThreadFavorite(@Field("formhash") String authenticityToken, @Field("id") String threadId, @Field("description") String remark);

    @FormUrlEncoded
    @POST(ApiHome.URL_THREAD_FAVOURITES_REMOVE)
    Single<AccountResultWrapper> removeThreadFavorite(@Field("formhash") String authenticityToken, @Field("favid") String favId);
    //endregion

    //region Reply
    @FormUrlEncoded
    @POST(ApiForum.URL_REPLY)
    Single<AccountResultWrapper> reply(@Field("formhash") String authenticityToken, @Field("tid") String threadId, @Field("message") String reply);

    @GET(ApiForum.URL_QUOTE_HELPER)
    Single<String> getQuoteInfo(@Query("tid") String threadId, @Query("repquote") String quotePostId);

    @FormUrlEncoded
    @POST(ApiForum.URL_REPLY)
    Single<AccountResultWrapper> replyQuote(@Field("formhash") String authenticityToken, @Field("tid") String threadId, @Field("message") String reply,
                                            @Field("noticeauthor") String encodedUserId, @Field("noticetrimstr") String quoteMessage, @Field("noticeauthormsg") String replyNotification);
    //endregion

    //region PM
    @GET(ApiHome.URL_PM_LIST)
    Single<BaseDataWrapper<PmGroups>> getPmGroups(@Query("page") int page);

    @GET(ApiHome.URL_PM_VIEW_LIST)
    Single<PmsWrapper> getPmList(@Query("touid") String toUid, @Query("page") int page);

    @FormUrlEncoded
    @POST(ApiHome.URL_PM_POST)
    Single<AccountResultWrapper> postPm(@Field("formhash") String authenticityToken, @Field("touid") String toUid, @Field("message") String msg);
    //endregion

    //region New thread
    @GET(ApiForum.URL_NEW_THREAD_HELPER)
    Single<String> getNewThreadInfo(@Query("fid") int fid);

    @FormUrlEncoded
    @POST(ApiForum.URL_NEW_THREAD)
    Single<AccountResultWrapper> newThread(@Query("fid") int fid, @Field("formhash") String authenticityToken, @Field("posttime") long postTime,
                                           @Field("typeid") String typeId, @Field("subject") String subject, @Field("message") String message,
                                           @Field("allownoticeauthor") int allowNoticeAuthor, @Field("usesig") int useSign,
                                           @Field("save") Integer saveAsDraft);
    //endregion

    @GET(ApiForum.URL_EDIT_POST_HELPER)
    Single<String> getEditPostInfo(@Query("fid") int fid, @Query("tid") int tid, @Query("pid") int pid);

    @FormUrlEncoded
    @POST(ApiForum.URL_EDIT_POST)
    Single<String> editPost(@Field("fid") int fid, @Field("tid") int tid, @Field("pid") int pid,
                            @Field("formhash") String authenticityToken, @Field("posttime") long postTime,
                            @Field("typeid") String typeId, @Field("subject") String subject,
                            @Field("message") String message, @Field("allownoticeauthor") int allowNoticeAuthor,
                            @Field("usesig") int useSign, @Field("save") Integer saveAsDraft,
                            @Field("readperm") String readPerm);

    @FormUrlEncoded
    @POST(ApiForum.URL_SEARCH_FORUM)
    Single<String> searchForum(@Field("formhash") String authenticityToken, @Field("srchtxt") String text);

    @FormUrlEncoded
    @POST(ApiForum.URL_SEARCH_USER)
    Single<String> searchUser(@Field("formhash") String authenticityToken, @Field("srchtxt") String text);

    //region User home
    @GET(ApiHome.URL_MY_NOTE_LIST)
    Single<BaseDataWrapper<Notes>> getMyNotes(@Query("page") int page);

    @GET(ApiHome.URL_PROFILE)
    Single<BaseDataWrapper<Profile>> getProfile(@Query("uid") String uid);

    @GET(ApiHome.URL_PROFILE_WEB)
    Single<String> getProfileWeb(@Header("Referer") String referer, @Query("uid") String uid);

    @GET(ApiHome.URL_FRIENDS)
    Single<BaseDataWrapper<Friends>> getFriends(@Query("uid") String uid);

    @GET(ApiHome.URL_THREADS)
    Single<String> getHomeThreads(@Query("uid") String uid, @Query("page") int page);

    @GET(ApiHome.URL_REPLIES)
    Single<String> getHomeReplies(@Query("uid") String uid, @Query("page") int page);

    //endregion

    @GET(ApiHome.URL_RATE_PRE)
    Single<String> getRatePreInfo(@Query("tid") String threadId, @Query("pid") String postId, @Query("t") long timestamp);

    @FormUrlEncoded
    @POST(ApiHome.URL_RATE)
    Single<String> rate(@Field("formhash") String authenticityToken, @Field("tid") String threadId, @Field("pid") String postId
            , @Field("referer") String refer, @Field("handlekey") String handleKey, @Field("score1") String score, @Field("reason") String reason);

    @GET(ApiHome.URL_REPORT_PRE)
    Single<String> getReportPreInfo(@Query("tid") String threadId, @Query("rid") String postId, @Query("t") long timestamp);

    @FormUrlEncoded
    @POST(ApiHome.URL_REPORT)
    Single<String> report(@FieldMap Map<String, String> fields, @Field("report_select") String report, @Field("message") String msg);

    @GET(ApiMember.URL_AUTO_SIGN)
    Single<String> autoSign(@Query("formhash") String authenticityToken);

    @FormUrlEncoded
    @POST(ApiForum.URL_VOTE)
    Single<String> vote(@Query("tid") String threadId, @Field("formhash") String authenticityToken
            , @Field("pollanswers[]") List<Integer> answers);

    @GET(ApiForum.URL_RATE_LIST)
    Single<String> getRates(@Query("tid") String threadId, @Query("pid") String postId);

    @GET(Api.URL_DARK_ROOM)
    Single<DarkRoomWrapper> getDarkRoom(@Query("cid") String cid);

    @GET(Api.URL_WEB_BLACK_LIST)
    Single<String> getWebBlackList(@Query("uid") String uid, @Query("page") int page);
}
