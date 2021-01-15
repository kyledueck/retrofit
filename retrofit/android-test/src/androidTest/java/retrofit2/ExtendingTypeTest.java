package retrofit2;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ExtendingTypeTest
{
    @Rule
    public final MockWebServer server = new MockWebServer();

    interface TypeParam<RequestType, ReturnType> {
        ReturnType method(RequestType request);
    }

    interface ExtendingTypeParam extends TypeParam<String, Call<ResponseBody>>
    {
        @GET("/")
        @Override
        Call<ResponseBody> method(@Query("foo") String request);
    }

    @Test
    public void extendingType() throws IOException
    {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(server.url("/"))
            .build();
        ExtendingTypeTest.ExtendingTypeParam example = retrofit.create(ExtendingTypeTest.ExtendingTypeParam.class);

        server.enqueue(new MockResponse().setBody("1234"));

        Response<ResponseBody> response = example.method("request").execute();
        assertEquals("1234", response.body().string());
    }
}
