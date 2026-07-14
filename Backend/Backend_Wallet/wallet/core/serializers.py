from rest_framework import serializers
from .models import User ,Wallet , FundRequest , QRCode , Message , ChatRoom ,SavedPaymentDetails , Referral
from django.contrib.auth import authenticate
from rest_framework import serializers

from rest_framework import serializers
from .models import User


class RegisterSerializer(serializers.ModelSerializer):

    referral_code = serializers.CharField(
        required=False,
        allow_blank=True
    )

    class Meta:
        model = User
        fields = [
            'full_name',
            'mobile_number',
            'password',
            'referral_code'
        ]

        extra_kwargs = {
            'password': {'write_only': True}
        }

class ReferralHistorySerializer(serializers.ModelSerializer):

    name = serializers.CharField(source="referred_user.full_name")
    mobile_number = serializers.CharField(source="referred_user.mobile_number")

    class Meta:
        model = Referral
        fields = [
            "name",
            "mobile_number",
            "reward",
            "created_at"
        ]

class SavedPaymentDetailsSerializer(serializers.ModelSerializer):

    class Meta:
        model = SavedPaymentDetails
        fields = [
            "id",
            "user",
            "account_name",
            "upi_id",
            "qr_code",
            "is_default",
            "created_at"
        ]

        read_only_fields = [
            "id",
            "created_at"
        ]

    def validate(self, attrs):

        if not attrs.get("upi_id") and not attrs.get("qr_code"):
            raise serializers.ValidationError(
                "Provide either a UPI ID or a QR Code."
            )

        return attrs

class FundRequestSerializer(serializers.ModelSerializer):

    user_name = serializers.CharField(
        source='user.full_name',
        read_only=True
    )

    mobile_number = serializers.CharField(
        source='user.mobile_number',
        read_only=True
    )
    admin_name = serializers.CharField(
    source='admin.full_name',
    read_only=True
    )

    admin_mobile = serializers.CharField(
    source='admin.mobile_number',
    read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
        'id',
        'user',
        'admin',
        'user_name',
        'mobile_number',
        'admin_name',
        'admin_mobile',
        'amount',
        'utr_number',
        'upi_id',
        'qr_code',
        'request_type',
        'status',
        'created_at'
        ]
        read_only_fields = [
            'request_type',
            'status',
            'created_at',
            'user_name',
            'mobile_number',
            'admin_name',
            'admin_mobile',
        ]


class UserListSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'role',
            'wallet_balance'
        ]

class UserDetailSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    total_requests = serializers.SerializerMethodField()

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'role',
            'wallet_balance',
            'total_requests'
        ]

    def get_total_requests(self, obj):
        return obj.fund_requests.count()
    

class UserRequestHistorySerializer(serializers.ModelSerializer):

    class Meta:
        model = FundRequest
        fields = [
            'id',
            'amount',
            'request_type',
            'status',
            'created_at'
        ]


class UserDashboardSerializer(serializers.ModelSerializer):

    wallet_balance = serializers.DecimalField(
        source='wallet.balance',
        max_digits=12,
        decimal_places=2,
        read_only=True
    )

    total_requests = serializers.SerializerMethodField()
    pending_requests = serializers.SerializerMethodField()

    class Meta:
        model = User
        fields = [
            'id',
            'full_name',
            'mobile_number',
            'wallet_balance',
            'total_requests',
            'pending_requests'
        ]

    def get_total_requests(self, obj):
        return obj.fund_requests.count()

    def get_pending_requests(self, obj):
        return obj.fund_requests.filter(
            status='PENDING'
        ).count()
    

class QRCodeSerializer(serializers.ModelSerializer):

    class Meta:
        model = QRCode
        fields = [
            'id',
            'image'
        ]

class TransactionHistorySerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(
        source="user.full_name",
        read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
            "id",
            "user_id",
            "user_name",
            "amount",
            "request_type",
            "status",
            "created_at"
        ]

class FundApprovedSerializers(serializers.ModelSerializer):

    user_name = serializers.CharField(
        source='user.full_name',
        read_only=True
    )

    mobile_number = serializers.CharField(
        source='user.mobile_number',
        read_only=True
    )

    class Meta:
        model = FundRequest
        fields = [
            'id',
            'user',
            'user_name',
            'mobile_number',
            'amount',
            'request_type',
            'status',
            'created_at'
        ]


class MessageSerializer(serializers.ModelSerializer):

    sender_name = serializers.CharField(
        source='sender.full_name',
        read_only=True
    )

    sender_role = serializers.CharField(
        source='sender.role',
        read_only=True
    )

    class Meta:

        model = Message

        fields = '__all__'

class ChatRoomSerializer(
    serializers.ModelSerializer
):

    class Meta:

        model = ChatRoom

        fields = "__all__"


class AddBonusSerializer(serializers.Serializer):
    amount = serializers.DecimalField(
        max_digits=12,
        decimal_places=2
    )