from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.hashers import check_password

from .models import User, Wallet , FundRequest ,QRCode , ChatRoom , Message , SavedPaymentDetails , Referral
from .serializers import RegisterSerializer ,QRCodeSerializer,FundApprovedSerializers,MessageSerializer, ReferralHistorySerializer,SavedPaymentDetailsSerializer ,FundRequestSerializer, TransactionHistorySerializer,UserDashboardSerializer , UserListSerializer , UserRequestHistorySerializer,UserDetailSerializer
from django.contrib.auth import authenticate
from django.shortcuts import get_object_or_404
from rest_framework import status
from decimal import Decimal
import random
from django.db.models import Q
import string


@api_view(['POST'])
def register(request):

    serializer = RegisterSerializer(data=request.data)

    if not serializer.is_valid():
        return Response(
            serializer.errors,
            status=400
        )

    full_name = serializer.validated_data['full_name']
    mobile_number = serializer.validated_data['mobile_number']
    password = serializer.validated_data['password']
    referral_code = serializer.validated_data.get("referral_code", "").strip()

    referrer = None

    if referral_code:
        try:
            referrer = User.objects.get(referral_code=referral_code)
        except User.DoesNotExist:
            return Response({
                "error": "Invalid referral code"
            }, status=400)


    if User.objects.filter(mobile_number=mobile_number).exists():
        return Response({
            "error": "Mobile number already exists"
        }, status=400)

    user = User.objects.create_user(
        mobile_number=mobile_number,
        password=password
    )

    # CHANGED
    user.full_name = full_name
    user.referral_code = generate_referral_code()
    user.save()

    Wallet.objects.create(
        user=user
    )
    if referrer:

        user.referred_by = referrer
        user.save()

        referrer.wallet.balance += 500
        referrer.wallet.save()

        user.wallet.balance += 500
        user.wallet.save()

        Referral.objects.create(
            referrer=referrer,
            referred_user=user,
            reward=500
        )

    return Response({
        "message": "User registered successfully",
        "user_id": user.id,
        "my_referral_code": user.referral_code      # opti for testing
    })

#Login
@api_view(['POST'])
def login(request):

    mobile_number = request.data.get('mobile_number')
    password = request.data.get('password')

    if not mobile_number or not password:
        return Response(
            {"error": "Mobile number and password required"},
            status=400
        )

    user = authenticate(
        request,
        mobile_number=mobile_number,
        password=password
    )

    if user is None:
        return Response(
            {"error": "Invalid credentials"},
            status=401
        )

    wallet = Wallet.objects.get(user=user)

    return Response({
        "message": "Login successful",
        "user_id": user.id,
        "full_name": user.full_name,
        "mobile_number": user.mobile_number,
        "role": user.role,
        "wallet_balance": wallet.balance
    })

#Delete User
@api_view(['DELETE'])
def delete_user(request, user_id):

    try:
        user = User.objects.get(id=user_id)

        user.delete()

        return Response(
            {
                "message": "User deleted successfully"
            },
            status=status.HTTP_200_OK
        )

    except User.DoesNotExist:

        return Response(
            {
                "error": "User not found"
            },
            status=status.HTTP_404_NOT_FOUND
        )

#Add Fund Request
@api_view(['POST'])
def add_fund_request(request):

    serializer = FundRequestSerializer(
        data=request.data
    )

    if serializer.is_valid():

        serializer.save(
            request_type='ADD'
        )

        return Response({
            "message": "Add fund request submitted"
        })

    print(serializer.errors)

    return Response(
        serializer.errors,
        status=400
    )

@api_view(['POST'])
def withdraw_fund_request(request):

    serializer = FundRequestSerializer(data=request.data)

    if serializer.is_valid():

        payment = serializer.validated_data.get("payment_account")

        serializer.save(
            request_type="WITHDRAW",
            upi_id=payment.upi_id if payment else None,
            qr_code=payment.qr_code if payment else None
        )

        return Response({
            "message": "Withdraw fund request submitted"
        })

    print(serializer.errors)

    return Response(serializer.errors, status=400)

# For Approve Request
@api_view(['POST'])
def approve_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    wallet = Wallet.objects.get(
        user=fund_request.user
    )

    if fund_request.request_type == 'ADD':

        wallet.balance += fund_request.amount

    else:

        wallet.balance -= fund_request.amount

    wallet.save()

    fund_request.status = 'APPROVED'
    fund_request.save()

    return Response({
        "message": "Request approved successfully"
    })


# For Reject Request
@api_view(['POST'])
def reject_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    fund_request.status = 'REJECTED'
    fund_request.save()

    return Response({
        "message": "Request rejected successfully"
    })

#FOr Reject Request
@api_view(['POST'])
def reject_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    fund_request.status = 'REJECTED'
    fund_request.save()

    return Response({
        "message": "Request rejected successfully"
    })

#Approved Request
@api_view(['GET'])
def approved_requests(request):

    requests = FundRequest.objects.filter(
        status='APPROVED'
    ).order_by('-created_at')

    serializer = FundApprovedSerializers(
        requests,
        many=True
    )

    return Response(serializer.data)


#admin dashboard
@api_view(['GET'])
def admin_dashboard(request, admin_id):

    try:
        admin = User.objects.get(id=admin_id)
    except User.DoesNotExist:
        return Response({"error": "User not found"}, status=404)

    if admin.role == "SUPER_ADMIN":

        total_users = User.objects.filter(
            role="ADMIN"
        ).count()

        pending_requests = FundRequest.objects.filter(
            status="PENDING"
        ).count()

        approved_requests = FundRequest.objects.filter(
            status="APPROVED"
        ).count()

        rejected_requests = FundRequest.objects.filter(
            status="REJECTED"
        ).count()

    else:

        total_users = User.objects.filter(
            role="MEMBER"
        ).count()

        pending_requests = FundRequest.objects.filter(
            admin=admin,
            status="PENDING"
        ).count()

        approved_requests = FundRequest.objects.filter(
            admin=admin,
            status="APPROVED"
        ).count()

        rejected_requests = FundRequest.objects.filter(
            admin=admin,
            status="REJECTED"
        ).count()

    return Response({
        "total_users": total_users,
        "pending_requests": pending_requests,
        "approved_requests": approved_requests,
        "rejected_requests": rejected_requests
    })

#Pending Request 
@api_view(['GET'])
def pending_requests(request, admin_id):

    requests = FundRequest.objects.filter(
        admin_id=admin_id,
        status="PENDING"
    ).order_by("-created_at")

    serializer = FundRequestSerializer(
        requests,
        many=True
    )

    return Response(serializer.data)

#All Users list 
@api_view(['GET'])
def all_users(request, user_id):

    try:
        current_user = User.objects.get(id=user_id)
    except User.DoesNotExist:
        return Response(
            {"error": "User not found"},
            status=404
        )

    if current_user.role == "ADMIN":

        users = User.objects.filter(
            role="MEMBER"
        ).order_by("-id")

    elif current_user.role == "SUPER_ADMIN":

        users = User.objects.filter(
            role="ADMIN"
        ).order_by("-id")

    else:
        return Response(
            {"error": "Permission denied"},
            status=403
        )

    serializer = UserListSerializer(users, many=True)

    return Response(serializer.data)

#User Details
@api_view(['GET'])
def user_details(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    serializer = UserDetailSerializer(user)

    return Response(serializer.data)

@api_view(['GET'])
def search_user(request, user_id):

    search = request.GET.get("search")

    if not search:
        return Response(
            {"error": "Search value required"},
            status=400
        )

    try:
        current_user = User.objects.get(id=user_id)
    except User.DoesNotExist:
        return Response(
            {"error": "User not found"},
            status=404
        )

    if current_user.role == "SUPER_ADMIN":

        users = User.objects.filter(
            role="ADMIN"
        ).filter(
            Q(full_name__icontains=search) |
            Q(mobile_number__icontains=search)
        )

    elif current_user.role == "ADMIN":

        users = User.objects.filter(
            role="MEMBER"
        ).filter(
            Q(full_name__icontains=search) |
            Q(mobile_number__icontains=search)
        )

    else:

        return Response(
            {"error": "Permission denied"},
            status=403
        )

    serializer = UserListSerializer(users, many=True)

    return Response(serializer.data)


#User Fund Request History
@api_view(['GET'])
def user_request_history(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    requests = FundRequest.objects.filter(
        user=user
    ).order_by('-created_at')

    serializer = UserRequestHistorySerializer(
        requests,
        many=True
    )

    return Response(serializer.data)



#USer DashBoard 
@api_view(['GET'])
def user_dashboard(request, id):

    user = get_object_or_404(
        User,
        id=id
    )

    serializer = UserDashboardSerializer(user)

    return Response(serializer.data)







#Most important thing QRCODE 

#Upload QR CODE
@api_view(['POST'])
def upload_qr_code(request):

    admin_id = request.data.get("admin")

    try:
        admin = User.objects.get(
            id=admin_id,
            role__in=["ADMIN", "SUPER_ADMIN"]
        )

    except User.DoesNotExist:

        return Response(
            {"error": "Admin not found"},
            status=404
        )

    qr = QRCode.objects.filter(
        admin=admin
    ).first()

    if qr:
        qr.image = request.FILES["image"]
        qr.save()

    else:
        QRCode.objects.create(
            admin=admin,
            image=request.FILES["image"]
        )

    return Response({
        "message": "QR Code uploaded successfully"
    })

# View QR Code 
@api_view(["GET"])
def get_qr_code(request):

    admin_id = request.GET.get("admin_id")

    qr = QRCode.objects.filter(
        admin_id=admin_id
    ).first()

    if not qr:
        return Response(
            {"error": "QR not found"},
            status=404
        )

    serializer = QRCodeSerializer(qr)

    return Response(serializer.data)

#History 
@api_view(["GET"])
def all_transactions(request, admin_id):

    try:
        admin = User.objects.get(id=admin_id)
    except User.DoesNotExist:
        return Response(
            {"error": "User not found"},
            status=404
        )

    if admin.role == "SUPER_ADMIN":

        transactions = FundRequest.objects.all().order_by(
            "-created_at"
        )

    else:

        transactions = FundRequest.objects.filter(
            admin=admin
        ).order_by(
            "-created_at"
        )

    serializer = TransactionHistorySerializer(
        transactions,
        many=True
    )

    return Response(serializer.data)


#Chat System

@api_view(['POST'])
def create_chat_room(request):

    user_id = request.data.get("user")
    admin_id = request.data.get("admin")

    try:
        user = User.objects.get(id=user_id)

    except User.DoesNotExist:

        return Response(
            {
                "error": "User not found"
            },
            status=404
        )

    try:
        admin = User.objects.get(
            id=admin_id,
            role__in=["ADMIN", "SUPER_ADMIN"]
        )

    except User.DoesNotExist:

        return Response(
            {
                "error": "Admin not found"
            },
            status=404
        )

    room, created = ChatRoom.objects.get_or_create(
        user=user,
        admin=admin
    )

    return Response(
        {
            "room_id": room.id,
            "created": created
        }
    )

@api_view(['POST'])
def send_message(request):

    serializer = MessageSerializer(
        data=request.data
    )

    if serializer.is_valid():

        serializer.save()

        return Response({
            "message": "Message sent"
        })

    return Response(
        serializer.errors,
        status=400
    )

@api_view(['GET'])
def get_chat_messages(request, room_id):

    messages = Message.objects.filter(
        room_id=room_id
    ).order_by('created_at')

    serializer = MessageSerializer(
        messages,
        many=True
    )

    return Response(serializer.data)

#Chat list to the admin
@api_view(['GET'])
def get_chat_rooms(request):

    admin_id = request.GET.get("admin_id")

    try:
        admin = User.objects.get(
            id=admin_id,
            role__in=["ADMIN", "SUPER_ADMIN"]
        )

    except User.DoesNotExist:

        return Response(
            {
                "error": "Admin not found"
            },
            status=404
        )

    rooms = ChatRoom.objects.filter(
        admin=admin
    ).order_by("-updated_at")

    data = []

    for room in rooms:

        data.append({
            "room_id": room.id,
            "user_id": room.user.id,
            "full_name": room.user.full_name,
            "mobile_number": room.user.mobile_number,
            "updated_at": room.updated_at
        })

    return Response(data)

@api_view(['GET'])
def get_chat_room_messages(request, room_id):

    messages = Message.objects.filter(
        room_id=room_id
    ).order_by('created_at')

    serializer = MessageSerializer(
        messages,
        many=True
    )

    return Response(serializer.data)

#add Bonus
@api_view(['POST'])
def add_bonus(request):

    user_id = request.data.get("user_id")
    bonus_amount = request.data.get("bonus_amount")

    if not user_id or not bonus_amount:
        return Response(
            {
                "status": False,
                "message": "user_id and bonus_amount are required"
            },
            status=status.HTTP_400_BAD_REQUEST
        )

    try:
        user = User.objects.get(id=user_id)
        wallet = Wallet.objects.get(user=user)

        wallet.balance += Decimal(str(bonus_amount))
        wallet.save()

        FundRequest.objects.create(
        user=user,
        amount=Decimal(str(bonus_amount)),
        request_type='BONUS',
        status='APPROVED'
        )

        return Response({
            "status": True,
            "message": "Bonus added successfully",
            "balance": str(wallet.balance)
        })

    except User.DoesNotExist:
        return Response(
            {
                "status": False,
                "message": "User not found"
            },
            status=status.HTTP_404_NOT_FOUND
        )

    except Wallet.DoesNotExist:
        return Response(
            {
                "status": False,
                "message": "Wallet not found"
            },
            status=status.HTTP_404_NOT_FOUND
        )
    

#Add Payment Details
@api_view(['POST'])
def add_payment_account(request):

    serializer = SavedPaymentDetailsSerializer(
        data=request.data
    )

    if serializer.is_valid():

        user = serializer.validated_data["user"]

        if serializer.validated_data.get("is_default", False):

            SavedPaymentDetails.objects.filter(
                user=user
            ).update(
                is_default=False
            )

        serializer.save()

        return Response(
            {
                "status": True,
                "message": "Payment account added successfully."
            }
        )

    return Response(
        serializer.errors,
        status=status.HTTP_400_BAD_REQUEST
    )

#Get User Payment Accounts
@api_view(['GET'])
def payment_accounts(request, user_id):

    accounts = SavedPaymentDetails.objects.filter(
        user_id=user_id
    ).order_by(
        "-is_default",
        "-created_at"
    )

    serializer = SavedPaymentDetailsSerializer(
        accounts,
        many=True
    )

    return Response(serializer.data)

# Update Payment Account
@api_view(['PUT'])
def update_payment_account(request):

    account_id = request.data.get("id")

    try:

        account = SavedPaymentDetails.objects.get(
            id=account_id
        )

    except SavedPaymentDetails.DoesNotExist:

        return Response(
            {
                "message": "Payment account not found."
            },
            status=404
        )

    serializer = SavedPaymentDetailsSerializer(
        account,
        data=request.data,
        partial=True
    )

    if serializer.is_valid():

        if serializer.validated_data.get("is_default", False):

            SavedPaymentDetails.objects.filter(
                user=account.user
            ).update(
                is_default=False
            )

        serializer.save()

        return Response(
            {
                "status": True,
                "message": "Payment account updated successfully."
            }
        )

    return Response(
        serializer.errors,
        status=400
    )

#Delete Payment Account

@api_view(['DELETE'])
def delete_payment_account(request, account_id):

    try:
        account = SavedPaymentDetails.objects.get(
            id=account_id
        )

        account.delete()

        return Response({
            "status": True,
            "message": "Payment account deleted successfully."
        })

    except SavedPaymentDetails.DoesNotExist:

        return Response({
            "status": False,
            "message": "Payment account not found."
        }, status=404)

# Set Default Payment Account
@api_view(['POST'])
def set_default_payment_account(request):

    account_id = request.data.get("id")

    try:

        account = SavedPaymentDetails.objects.get(
            id=account_id
        )

    except SavedPaymentDetails.DoesNotExist:

        return Response(
            {
                "message": "Payment account not found."
            },
            status=404
        )

    SavedPaymentDetails.objects.filter(
        user=account.user
    ).update(
        is_default=False
    )

    account.is_default = True
    account.save()

    return Response(
        {
            "status": True,
            "message": "Default payment account updated."
        }
    )

def generate_referral_code():
    while True:
        code = ''.join(
            random.choices(
                string.ascii_uppercase + string.digits,
                k=8
            )
        )

        if not User.objects.filter(referral_code=code).exists():
            return code
        
#get payment accounts
@api_view(["GET"])
def get_payment_accounts(request, user_id):

    accounts = SavedPaymentDetails.objects.filter(
        user_id=user_id
    ).order_by("-is_default", "-created_at")

    data = []

    for account in accounts:

        data.append({

            "id": account.id,

            "account_name": account.account_name,

            "upi_id": account.upi_id,

            "qr_code": request.build_absolute_uri(
                account.qr_code.url
            ) if account.qr_code else None,

            "is_default": account.is_default

        })

    return Response(data)

#Referral 
@api_view(["GET"])
def my_referral(request, user_id):

    try:
        user = User.objects.get(id=user_id)

    except User.DoesNotExist:

        return Response({
            "error": "User not found"
        }, status=404)

    referrals = Referral.objects.filter(
        referrer=user
    ).order_by("-created_at")

    serializer = ReferralHistorySerializer(
        referrals,
        many=True
    )

    total_bonus = sum(
        referral.reward for referral in referrals
    )

    return Response({

        "my_referral_code": user.referral_code,
        "total_referrals": referrals.count(),
        "total_bonus": total_bonus,
        "referrals": serializer.data
    })

#SUPER ADMIN 
@api_view(['POST'])
def create_admin(request):

    super_admin_id = request.data.get("super_admin_id")

    super_admin = User.objects.get(
        id=super_admin_id
    )

    if super_admin.role != "SUPER_ADMIN":

        return Response({
            "error":"Only super admin can create admin"
        }, status=403)


    full_name = request.data.get("full_name")
    mobile = request.data.get("mobile_number")
    password = request.data.get("password")


    if User.objects.filter(
        mobile_number=mobile
    ).exists():

        return Response({
            "error":"Mobile already exists"
        }, status=400)


    admin = User.objects.create_user(
        mobile_number=mobile,
        password=password,
        role="ADMIN"
    )

    admin.full_name = full_name
    admin.save()

    Wallet.objects.create(
        user=admin,
        balance=0
    )


    return Response({

        "message":"Admin created successfully",

        "admin_id":admin.id
    })

#get all admin 
@api_view(['GET'])
def get_all_admins(request):

    admins = User.objects.filter(role="ADMIN")

    data = []

    for admin in admins:

        data.append({

            "id": admin.id,

            "full_name": admin.full_name,

            "mobile_number": admin.mobile_number

        })

    return Response(data)