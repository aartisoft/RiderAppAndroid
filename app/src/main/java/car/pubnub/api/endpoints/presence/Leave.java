package car.pubnub.api.endpoints.presence;

import car.pubnub.api.PubNub;
import car.pubnub.api.PubNubException;
import car.pubnub.api.PubNubUtil;
import car.pubnub.api.builder.PubNubErrorBuilder;
import car.pubnub.api.endpoints.Endpoint;
import car.pubnub.api.enums.PNOperationType;
import car.pubnub.api.managers.RetrofitManager;
import car.pubnub.api.managers.TelemetryManager;
import car.pubnub.api.models.server.Envelope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

@Accessors(chain = true, fluent = true)
public class Leave extends Endpoint<Envelope, Boolean> {
    private List<String> channels;
    private List<String> channelGroups;

    public Leave(PubNub pubnub, TelemetryManager telemetryManager, RetrofitManager retrofit) {
        super(pubnub, telemetryManager, retrofit);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub().getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (channels.size() == 0 && channelGroups.size() == 0) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CHANNEL_AND_GROUP_MISSING).build();
        }
    }

    @Override
    protected Call<Envelope> doWork(Map<String, String> params) {
        String channelCSV;

        if (channelGroups.size() > 0) {
            params.put("channel-group", PubNubUtil.joinString(channelGroups, ","));
        }

        if (channels.size() > 0) {
            channelCSV = PubNubUtil.joinString(channels, ",");
        } else {
            channelCSV = ",";
        }

        return this.getRetrofit().getPresenceService().leave(this.getPubnub().getConfiguration().getSubscribeKey(), channelCSV, params);
    }

    @Override
    protected Boolean createResponse(Response<Envelope> input) throws PubNubException {
        return true;
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNUnsubscribeOperation;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return channelGroups;
    }


    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public Leave channels(final List<String> channels) {
        this.channels = channels;
        return this;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public Leave channelGroups(final List<String> channelGroups) {
        this.channelGroups = channelGroups;
        return this;
    }

}
